package io.leerv.peach_note.statusTable.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusTableRenameRequest {
    @Min(value = 0, message = "id cannot be less than 0")
    @NotNull(message = "table id is required")
    private Long tableId;
    @Size(min = 2, max = 64, message = "table name length should be between 2 and 64 characters long")
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?!.*[^a-zA-Z0-9\\s]{2,})(?!.*\\s$).+$",
            message = "name should not contain repeating special characters or training spaces")
    private String name;
}
