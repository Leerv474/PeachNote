package io.leerv.peach_note.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardDto {
    private Long id;
    private String name;
    private List<Long> userIdList;
    private List<Long> statusTableIdList;
    private List<Long> projectIdList;
}
