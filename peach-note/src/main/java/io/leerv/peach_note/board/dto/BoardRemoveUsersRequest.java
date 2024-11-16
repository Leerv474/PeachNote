package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardRemoveUsersRequest {
    Long boardId;
    List<Long> userIdList;
}
