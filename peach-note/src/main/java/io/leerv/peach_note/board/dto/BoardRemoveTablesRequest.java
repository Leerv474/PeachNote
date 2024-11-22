package io.leerv.peach_note.board.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardRemoveTablesRequest {
    @NotNull
    @Min(0)
    private Long boardId;

    @NotNull
    private List<Long> removedTablesList;
}
