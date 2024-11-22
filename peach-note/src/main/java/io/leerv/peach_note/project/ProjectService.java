package io.leerv.peach_note.project;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.board.BoardUtil;
import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.permission.BoardPermissionUtil;
import io.leerv.peach_note.project.dto.*;
import io.leerv.peach_note.task.Task;
import io.leerv.peach_note.task.TaskMapper;
import io.leerv.peach_note.task.dto.TaskItemDto;
import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository repository;
    private final ProjectUtil projectUtil;
    private final TaskMapper taskMapper;
    private final BoardPermissionUtil boardPermissionUtil;
    private final BoardUtil boardUtil;

    public void create(User authenticatedUser, ProjectCreateRequest request) {
        if (boardPermissionUtil.userIsCreator(authenticatedUser.getId(), request.getBoardId())) {
            throw new OperationNotPermittedException("User does not have the rights to edit this project");
        }
        Board board = boardUtil.findBoardById(request.getBoardId());
        // todo: validation
        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .board(board)
                .taskList(new ArrayList<>())
                .build();
        repository.save(project);
    }

    public void delete(User authenticatedUser, Long projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new RecordNotFound("Project not found"));
        if (!boardPermissionUtil.userIsEditor(authenticatedUser.getId(), project.getBoard().getId())) {
            throw new OperationNotPermittedException("User does not have the rights to edit this project");
        }
        repository.delete(project);
    }

    public void edit(User authenticatedUser, ProjectEditRequest request) {
        Project project = repository.findById(request.getProjectId())
                .orElseThrow(() -> new RecordNotFound("Project not found"));
        if (!boardPermissionUtil.userIsEditor(authenticatedUser.getId(), project.getBoard().getId())) {
            throw new OperationNotPermittedException("User does not have the rights to edit this project");
        }
        boolean changed = false;
        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
            changed = true;
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
            changed = true;
        }
        if (request.getDeadLine() != null) {
            project.setDeadline(request.getDeadLine());
            changed = true;
        }
        if (!changed) {
            return;
        }
        repository.save(project);
    }


    public ProjectTaskListResponse getTaskList(User authenticatedUser, Long projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new RecordNotFound("Project not found"));
        if (!boardPermissionUtil.userHasAccess(authenticatedUser.getId(), project.getBoard().getId())) {
            throw new OperationNotPermittedException("User does not have the rights to view this project");
        }
        List<TaskItemDto> taskList = project.getTaskList().stream().map(taskMapper::mapToTaskItemDto).toList();
        return ProjectTaskListResponse.builder()
                .projectId(projectId)
                .taskList(taskList)
                .build();
    }

    public ProjectProgressResponse getProgress(User authenticatedUser, Long projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new RecordNotFound("Project not found"));
        if (!boardPermissionUtil.userHasAccess(authenticatedUser.getId(), projectId)) {
            throw new OperationNotPermittedException("User does not have the rights to view this project");
        }
        List<Task> taskList = project.getTaskList();
        int finishedTasks = 0;
        for (Task task : taskList) {
            if (task.getStatusTable().getName().equals("Done")) {
                finishedTasks++;
            }
        }
        return ProjectProgressResponse.builder()
                .projectId(projectId)
                .tasksAmount(project.getTaskList().size())
                .finishedTasks(finishedTasks)
                .build();
    }

    public ProjectDto view(User user, Long projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new RecordNotFound("Project not found"));
        if (!boardPermissionUtil.userHasAccess(user.getId(), projectId)) {
            throw new OperationNotPermittedException("User does not have the rights to view this project");
        }
        int finishedTasks = projectUtil.countFinishedTasks(project);
        return ProjectDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .deadline(project.getDeadline())
                .tasksAmount(project.getTaskList().size())
                .finishedTasks(finishedTasks)
                .build();
    }
}
