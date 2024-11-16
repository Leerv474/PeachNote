package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardRemoveTablesRequest {
    private Long boardId;
    private List<Long> removedTablesList;
}
