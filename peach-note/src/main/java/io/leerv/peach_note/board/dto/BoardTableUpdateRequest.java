package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BoardTableUpdateRequest {
    private Long boardId;
    private Map<Long, Integer> statusTableMap;
}
