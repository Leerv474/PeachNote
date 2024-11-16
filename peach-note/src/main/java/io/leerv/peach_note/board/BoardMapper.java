package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.BoardCreateResponse;
import io.leerv.peach_note.board.dto.BoardDto;
import io.leerv.peach_note.project.Project;
import io.leerv.peach_note.statusTable.StatusTable;

import java.util.List;
import java.util.Optional;

public class BoardMapper {
    public static BoardDto mapToBoardDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .name(board.getName())
                .userIdList(Optional.ofNullable(board.getBoardPermissionList())
                        .orElseGet(List::of)
                        .stream().map(foo -> foo.getUser().getId()).toList())
                .projectIdList(Optional.ofNullable(board.getProjectList())
                        .orElseGet(List::of)
                        .stream().map(Project::getId).toList())
                .statusTableIdList(Optional.ofNullable(board.getStatusTableList())
                        .orElseGet(List::of)
                        .stream().map(StatusTable::getId).toList())
                .build();
    }

    public static BoardCreateResponse mapToBoardCreateResponse(Board board) {
        return BoardCreateResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .build();
    }
}
