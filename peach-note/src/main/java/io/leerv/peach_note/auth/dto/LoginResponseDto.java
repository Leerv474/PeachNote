package io.leerv.peach_note.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;
}
