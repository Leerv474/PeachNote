package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BoardTablesMapRequest {
    private Long boardId;
    private Map<String, Integer> statusTableMap;
}
