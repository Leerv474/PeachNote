package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.BoardCreateResponse;
import io.leerv.peach_note.board.dto.BoardDataResponse;
import io.leerv.peach_note.board.dto.BoardDto;
import io.leerv.peach_note.board.dto.BoardSimpleDto;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.project.ProjectMapper;
import io.leerv.peach_note.statusTable.StatusTable;
import io.leerv.peach_note.statusTable.StatusTableMapper;
import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;
import io.leerv.peach_note.user.UserMapper;
import io.leerv.peach_note.user.dto.UserPermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardMapper {
    private final ProjectMapper projectMapper;
    private final StatusTableMapper statusTableMapper;
    private final UserMapper userMapper;

    public BoardDto mapToBoardDto(Board board) {
        return BoardDto.builder()
                .boardId(board.getId())
                .name(board.getName())
                .userIdList(Optional.ofNullable(board.getBoardPermissionList())
                        .orElseGet(List::of)
                        .stream().map(foo -> foo.getUser().getId()).toList())
                .projectList(board.getProjectList() != null ? board.getProjectList().stream().map(projectMapper::mapToProjectItemDto).toList() : null)
                .statusTableList(board.getStatusTableList().stream()
                        .map(statusTableMapper::mapToStatusTableItemDto)
                        .sorted(Comparator.comparingInt(StatusTableItemDto::getDisplayOrder)).toList())
                .build();
    }

    public BoardCreateResponse mapToBoardCreateResponse(Board board) {
        return BoardCreateResponse.builder()
                .boardId(board.getId())
                .name(board.getName())
                .build();
    }

    public BoardSimpleDto mapToBoardSimpleDto(Board board) {
        return BoardSimpleDto.builder()
                .boardId(board.getId())
                .name(board.getName())
                .build();
    }

    public BoardDataResponse mapToBoardDataDto(Board board, Long authenticatedUserId) {
        int currentUserPermissionLevel = board.getBoardPermissionList().stream()
                .filter(permission -> permission.getUser().getId().equals(authenticatedUserId)).findAny()
                .orElseThrow(() -> new IllegalRequestContentException("Unexpected user id"))
                .getPermission().getPermissionLevel();
        List<StatusTable> nonDefaultStatuses = board.getStatusTableList().stream()
                .filter((table -> !table.getName().equals("Done") && table.getDisplayOrder() > 3))
                .sorted(Comparator.comparingInt(StatusTable::getDisplayOrder)).toList();
        return BoardDataResponse.builder()
                .boardId(board.getId())
                .currentUserPermissionLevel(currentUserPermissionLevel)
                .name(board.getName())
                .userPermissionList(board.getBoardPermissionList().stream().map(
                        permissions -> UserPermissionDto.builder()
                                .username(permissions.getUser().getName())
                                .permissionLevel(permissions.getPermission().getPermissionLevel())
                                .build()
                ).toList())
                .statusTableList(nonDefaultStatuses.stream().map(statusTableMapper::mapToAdditionalTablesDto).toList())
                .build();
    }
}
