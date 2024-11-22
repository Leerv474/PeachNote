package io.leerv.peach_note.board.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BoardTableUpdateRequest {
    @NotNull
    @Min(0)
    private Long boardId;
    @NotNull
    private Map<Long, Integer> statusTableMap;
}
