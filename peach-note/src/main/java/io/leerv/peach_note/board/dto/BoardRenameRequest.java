package io.leerv.peach_note.board.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRenameRequest {
    @NotNull
    @Min(0)
    private Long boardId;

    @Size(min = 2, max = 64)
    @Pattern(regexp = "^(?!.*[^a-zA-Z0-9\\s]{2,})(?!.*\\s$).+$")
    private String name;
}
