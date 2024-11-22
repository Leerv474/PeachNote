package io.leerv.peach_note.project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProjectDto {
    private Long projectId;
    private String title;
    private String description;
    private LocalDate deadline;
    private Integer tasksAmount;
    private Integer finishedTasks;
}
