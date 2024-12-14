package io.leerv.peach_note.project.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProjectEditRequest {
    @Min(value = 0, message = "id cannot be less than 0")
    @NotNull(message = "project id is required")
    private Long projectId;
    @Size(min = 2, max = 64, message = "title length should be between 2 and 64 characters long")
    private String title;
    @Size(max = 512, message = "description max size is 512 characters")
    private String description;
    @Future(message = "deadline should be in the future, you know...")
    private LocalDate deadline;
}
