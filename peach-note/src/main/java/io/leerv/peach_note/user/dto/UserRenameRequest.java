package io.leerv.peach_note.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRenameRequest {
    @NotNull(message = "username is mandatory")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9_]*$", message = "username should not contain special characters nor non-latin characters")
    @NotBlank(message = "username is mandatory")
    private String username;
    private Long id;
}
