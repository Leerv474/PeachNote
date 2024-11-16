package io.leerv.peach_note.task.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate creationDate;
    private LocalDate deadline;
    private LocalDate completionDate;
    private Integer priority;
    private Long projectId;
    private Long statusTableId;
}
