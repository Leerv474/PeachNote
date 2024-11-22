package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardSimpleDto {
    private Long boardId;
    private String name;
}
