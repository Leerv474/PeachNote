package io.leerv.peach_note.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeleteRequest {
    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Pattern(regexp = "^[\\x00-\\x7F]+$", message = "password should not contain non-latin characters")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
