package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.BoardCreateResponse;
import io.leerv.peach_note.board.dto.BoardDto;
import io.leerv.peach_note.board.dto.BoardSimpleDto;
import io.leerv.peach_note.project.ProjectMapper;

import java.util.List;
import java.util.Optional;

public class BoardMapper {
    public static BoardDto mapToBoardDto(Board board) {
        return BoardDto.builder()
                .boardId(board.getId())
                .name(board.getName())
                .userIdList(Optional.ofNullable(board.getBoardPermissionList())
                        .orElseGet(List::of)
                        .stream().map(foo -> foo.getUser().getId()).toList())
                .projectList(board.getProjectList().stream().map(ProjectMapper::mapToProjectItemDto).toList())
                .build();
    }

    public static BoardCreateResponse mapToBoardCreateResponse(Board board) {
        return BoardCreateResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .build();
    }

    public static BoardSimpleDto mapToBoardSimpleDto(Board board) {
        return BoardSimpleDto.builder()
                .boardId(board.getId())
                .name(board.getName())
                .build();
    }
}
