package io.leerv.peach_note.project;

import io.leerv.peach_note.project.dto.ProjectItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {
    private final ProjectUtil projectUtil;

    public static ProjectItemDto mapToProjectItemDto(Project project) {
        return ProjectItemDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .tasksAmount(project.getTaskList().size())
                .finishedTasksAmount(projectUtil.countFinishedTasks(project))
                .build();
    }

    public Project
}
