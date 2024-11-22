package io.leerv.peach_note.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleTaskDto {
    private Long taskId;
    private String title;
    private Long priority;
}
