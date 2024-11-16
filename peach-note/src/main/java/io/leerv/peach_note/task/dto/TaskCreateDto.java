package io.leerv.peach_note.task.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskCreateDto {
    private String title;
    private String description;
    private Integer priority;
    private LocalDate deadline;
    private Long projectId;
    private Long boardId;
}
