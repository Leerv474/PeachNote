package io.leerv.peach_note.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {
    @NotNull(message = "username is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "username should not contain special characters nor non-latin characters")
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Email(message = "invalid email format")
    private String email;

    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Pattern(regexp = "^[\\x00-\\x7F]+$", message = "password should not contain non-latin characters")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
