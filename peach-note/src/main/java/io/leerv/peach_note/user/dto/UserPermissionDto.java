package io.leerv.peach_note.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPermissionDto {
    private String username;
    private Integer permissionLevel;
}
