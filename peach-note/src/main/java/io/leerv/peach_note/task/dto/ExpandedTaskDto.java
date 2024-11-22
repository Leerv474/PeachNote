package io.leerv.peach_note.task.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExpandedTaskDto {
    private Long taskId;
    private String title;
    private Long priority;
    private LocalDate deadline;
    private Long projectId;
    private String projectTitle;
}
