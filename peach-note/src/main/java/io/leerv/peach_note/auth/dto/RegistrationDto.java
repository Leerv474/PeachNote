package io.leerv.peach_note.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationDto {
    private String username;
    private String email;
    private String password;
}
