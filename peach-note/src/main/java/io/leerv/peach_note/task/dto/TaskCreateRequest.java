package io.leerv.peach_note.task.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskCreateRequest {
    @NotNull(message = "title is required")
    @NotBlank(message = "title is required")
    @Size(min = 2, max = 64, message = "title length should be between 2 and 64 characters long")
    private String title;
    @Size(max = 512, message = "description max size is 512 characters")
    private String description;
    @Future(message = "deadline should be in the future, you know...")
    private LocalDate deadline;
    @Min(value = 0, message = "id cannot be less than 0")
    private Long projectId;
    @NotNull(message = "board id is required")
    @Min(value = 0, message = "id cannot be less than 0")
    private Long boardId;
}
