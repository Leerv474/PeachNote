package io.leerv.peach_note.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BoardCreateRequest {

    @NotNull(message = "name is mandatory")
    @NotBlank(message = "name is mandatory")
    @Size(min = 2, max = 64)
    @Pattern(regexp = "^(?!.*[^a-zA-Z0-9\\s]{2,})(?!.*\\s$).+$")
    private String name;
    private Map<String, Integer> additionalStatusMap;
}
