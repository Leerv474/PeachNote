package io.leerv.peach_note.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectItemDto {
    private Long projectId;
    private String title;
    private Integer tasksAmount;
    private Integer finishedTasksAmount;
}
