package com.leerv474.peach_note.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTokensResponse {
    private String accessToken;
    private String refreshToken;
}
