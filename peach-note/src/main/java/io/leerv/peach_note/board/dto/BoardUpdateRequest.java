package io.leerv.peach_note.board.dto;


import io.leerv.peach_note.statusTable.dto.AdditionalTablesDto;
import io.leerv.peach_note.user.dto.UserPermissionDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardUpdateRequest {
    private Long boardId;
    private String name;
    private List<AdditionalTablesDto> additionalStatusList;
    private List<UserPermissionDto> userList;
}
