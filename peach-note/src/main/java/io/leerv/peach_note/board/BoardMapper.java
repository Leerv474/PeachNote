package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.BoardCreateResponse;
import io.leerv.peach_note.board.dto.BoardDto;
import io.leerv.peach_note.board.dto.BoardSimpleDto;
import io.leerv.peach_note.project.ProjectMapper;
import io.leerv.peach_note.statusTable.StatusTableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final ProjectMapper projectMapper;
    private final StatusTableMapper statusTableMapper;

    public BoardDto mapToBoardDto(Board board) {
        return BoardDto.builder()
                .boardId(board.getId())
                .name(board.getName())
                .userIdList(Optional.ofNullable(board.getBoardPermissionList())
                        .orElseGet(List::of)
                        .stream().map(foo -> foo.getUser().getId()).toList())
                .projectList(board.getProjectList().stream().map(projectMapper::mapToProjectItemDto).toList())
                .statusTableList(board.getStatusTableList().stream().map(statusTableMapper::mapToStatusTableItemDto).toList())
                .build();
    }

    public BoardCreateResponse mapToBoardCreateResponse(Board board) {
        return BoardCreateResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .build();
    }

    public BoardSimpleDto mapToBoardSimpleDto(Board board) {
        return BoardSimpleDto.builder()
                .boardId(board.getId())
                .name(board.getName())
                .build();
    }
}
