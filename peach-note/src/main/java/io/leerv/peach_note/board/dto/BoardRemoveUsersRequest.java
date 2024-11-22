package io.leerv.peach_note.board.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardRemoveUsersRequest {
    @NotNull
    @Min(0)
    Long boardId;

    @NotNull
    List<Long> userIdList;
}
