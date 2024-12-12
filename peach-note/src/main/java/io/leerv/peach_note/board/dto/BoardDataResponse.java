package io.leerv.peach_note.board.dto;

import io.leerv.peach_note.statusTable.dto.AdditionalTablesDto;
import io.leerv.peach_note.user.dto.UserPermissionDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardDataResponse {
    private Long boardId;
    private Integer currentUserPermissionLevel;
    private String name;
    private List<UserPermissionDto> userPermissionList;
    private List<AdditionalTablesDto> statusTableList;
}
