package io.leerv.peach_note.board.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class BoardUsersPermissionsRequest {
    @NotNull
    @Min(0)
    private Long boardId;

    @NotNull
    private Map<Long, String> usersPermissions;
}
