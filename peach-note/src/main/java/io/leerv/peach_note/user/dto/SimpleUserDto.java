package io.leerv.peach_note.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleUserDto {
    private Long userId;
    private String username;
}