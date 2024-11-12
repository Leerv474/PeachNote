package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardDto {
    private final String name;
    private final List<String> additionalStatusList;
}
