package io.leerv.peach_note.project;

import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectUtil {
    private final ProjectRepository repository;

    public void deleteAllProjects(List<Project> projectList) {
        repository.deleteAll(projectList);
    }

    public Project findById(Long projectId) {
        return repository.findById(projectId)
                .orElseThrow(() -> new RecordNotFound("Project not found"));
    }

    public void createFromTask(Task task) {
        Project project = Project.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .board(task.getStatusTable().getBoard())
                .build();
        repository.save(project);
    }

    public int countFinishedTasks(Project project) {
        int result = 0;
        for (Task task : project.getTaskList()) {
            if (task.getStatusTable().getName().equals("Done")) {
                result++;
            }
        }
        return result;
    }
}
