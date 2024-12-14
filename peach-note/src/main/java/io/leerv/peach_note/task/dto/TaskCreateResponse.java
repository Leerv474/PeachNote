package io.leerv.peach_note.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCreateResponse {
    private Long taskId;
    private String title;
}
