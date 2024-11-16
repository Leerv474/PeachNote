package io.leerv.peach_note.task;

import io.leerv.peach_note.permission.BoardPermissionRepository;
import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.project.Project;
import io.leerv.peach_note.project.ProjectRepository;
import io.leerv.peach_note.statusTable.StatusTable;
import io.leerv.peach_note.statusTable.StatusTableRepository;
import io.leerv.peach_note.task.dto.TaskCreateDto;
import io.leerv.peach_note.task.dto.TaskDto;
import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final StatusTableRepository statusTableRepository;
    private final BoardPermissionRepository boardPermissionRepository;
    private final ProjectRepository projectRepository;


    public TaskDto create(TaskCreateDto request, User user) {
        boolean isEditor = boardPermissionRepository.userIsEditorOfBoard(user.getId(), request.getBoardId());
        if (!isEditor) {
            throw new OperationNotPermittedException("User isn't an editor of the current board");
        }

        StatusTable statusTable = statusTableRepository.findBucketByBoardId(request.getBoardId())
                .orElseThrow(() -> new RecordNotFound("Unable to create a task, Bucket status not found"));

        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RecordNotFound("Project not found"));
        }

        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creationDate(LocalDate.now())
                .deadline(request.getDeadline())
                .priority(request.getPriority())
                .project(project)
                .statusTable(statusTable)
                .build();
        repository.save(newTask);
        return TaskMapper.mapToTaskDto(newTask);
    }

    public void putTaskIntoBucket(Task task, StatusTable bucket) {
        task.setStatusTable(bucket);
        repository.save(task);
    }

    public void deleteAllTasks(List<Task> taskList) {
        repository.deleteAll(taskList);
    }
}
