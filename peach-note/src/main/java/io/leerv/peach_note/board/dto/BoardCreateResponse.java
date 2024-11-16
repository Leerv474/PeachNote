package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardCreateResponse {
    private Long id;
    private String name;
}
