package io.leerv.peach_note.board.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BoardUsersPermissionsRequest {
    private Long boardId;
    private Map<Long, String> usersPermissions;
}
