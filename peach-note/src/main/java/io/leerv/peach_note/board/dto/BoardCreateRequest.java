package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BoardCreateRequest {
    private String name;
    private Map<String, Integer> additionalStatusMap;
}
