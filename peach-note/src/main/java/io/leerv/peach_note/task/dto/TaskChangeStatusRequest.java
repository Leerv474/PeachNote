package io.leerv.peach_note.task.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskChangeStatusRequest {
    @NotNull(message = "task id is required")
    @Min(value = 0, message = "id cannot be less than 0")
    private Long taskId;
    @NotNull(message = "status table id is required")
    @Min(value = 0, message = "id cannot be less than 0")
    private Long statusTableId;
}
