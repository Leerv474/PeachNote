package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.*;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.permission.BoardPermissionUtil;
import io.leerv.peach_note.project.ProjectUtil;
import io.leerv.peach_note.statusTable.StatusTable;
import io.leerv.peach_note.statusTable.StatusTableUtil;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import io.leerv.peach_note.user.dto.UserPermissionDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;
    private final BoardPermissionUtil boardPermissionUtil;
    private final StatusTableUtil statusTableUtil;
    private final UserRepository userRepository;
    private final ProjectUtil projectUtil;
    private final BoardMapper boardMapper;

    public BoardCreateResponse create(BoardCreateRequest request, User authenticatedUser) {
        Board board = Board.builder()
                .name(request.getName())
                .build();

        repository.save(board);
        boardPermissionUtil.grantCreatorPermissions(authenticatedUser, board);
        if (request.getUserList() == null) {
            return boardMapper.mapToBoardCreateResponse(board);
        }
        for (UserPermissionDto dto : request.getUserList()) {
            User user = userRepository.findUserByUsername(dto.getUsername()).orElseThrow(() -> new RecordNotFound("User not found"));
            if (dto.getPermissionLevel() == 1) {
                boardPermissionUtil.grantViewerPermissions(user, board);
            }
            if (dto.getPermissionLevel() == 2) {
                boardPermissionUtil.grantEditorPermissions(user, board);
            }
        }
        statusTableUtil.createStatusTables(board, request.getAdditionalStatusList());

        return boardMapper.mapToBoardCreateResponse(board);
    }

    public BoardDto find(Long boardId, User authenticatedUser) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionUtil.userHasAccess(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to view this board");
        }
        return boardMapper.mapToBoardDto(board);
    }

    public void delete(Long boardId, User authenticatedUser) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to delete this board");
        }
        statusTableUtil.deleteAllTables(board.getStatusTableList());
        projectUtil.deleteAllProjects(board.getProjectList());
        repository.delete(board);
    }

    @Transactional(rollbackOn = {
            IllegalRequestContentException.class,
            UsernameNotFoundException.class
    })
    public void addUsers(User authenticatedUser, Long boardId, Map<Long, String> usersPermissions) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit user list");
        }
        if (usersPermissions == null) {
            throw new IllegalRequestContentException("Invalid users list");
        }
        for (var userSet : usersPermissions.entrySet()) {
            Long userId = userSet.getKey();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            boolean permissionIsViewer = userSet.getValue().equals("VIEWER");
            boolean permissionIsEditor = userSet.getValue().equals("EDITOR");
            if (!permissionIsEditor && !permissionIsViewer) {
                throw new IllegalRequestContentException("Invalid permission name");
            }
            if (permissionIsEditor) {
                boardPermissionUtil.grantEditorPermissions(user, board);
            }
            if (permissionIsViewer) {
                boardPermissionUtil.grantViewerPermissions(user, board);
            }
        }
    }

    @Transactional(rollbackOn = {
            UsernameNotFoundException.class
    })
    public void removeUsers(User authenticatedUser, Long boardId, List<Long> userIdList) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit user list");
        }
        if (userIdList == null) {
            throw new IllegalRequestContentException("Invalid users list");
        }
        for (var userId : userIdList) {
            if (!userRepository.existsById(userId)) {
                throw new UsernameNotFoundException("User not found");
            }
            if (userId.equals(authenticatedUser.getId())) {
                throw new IllegalRequestContentException("Cannot remove oneself");
            }
            boardPermissionUtil.revokePermission(userId, boardId);
        }
    }

    @Transactional(rollbackOn = {
            IllegalRequestContentException.class,
            RecordNotFound.class
    })
    public void changeUserPermissions(User authenticatedUser, Long boardId, Map<Long, String> usersPermissions) {
        if (!repository.existsById(boardId)) {
            throw new RecordNotFound("Board not found");
        }
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit user list");
        }
        if (usersPermissions == null) {
            throw new IllegalRequestContentException("Invalid users list");
        }
        for (var userSet : usersPermissions.entrySet()) {
            Long userId = userSet.getKey();
            boolean permissionIsViewer = userSet.getValue().equals("VIEWER");
            boolean permissionIsEditor = userSet.getValue().equals("EDITOR");
            if (!permissionIsEditor && !permissionIsViewer) {
                throw new IllegalRequestContentException("Invalid permission name");
            }
            if (permissionIsEditor) {
                boardPermissionUtil.setEditorPermission(userId, boardId);
            }
            if (permissionIsViewer) {
                boardPermissionUtil.setViewerPermission(userId, boardId);
            }
        }
    }

    public void addTables(User authenticatedUser, Long boardId, Map<String, Integer> statusTableMap) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board Not Found"));
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit status tables list");
        }
        if (statusTableMap == null) {
            throw new IllegalRequestContentException("Invalid status table list");
        }
        statusTableUtil.addStatusTables(board, statusTableMap);
    }

    public void removeTables(User authenticatedUser, Long boardId, List<Long> removedTablesList) {
        if (!repository.existsById(boardId)) {
            throw new RecordNotFound("Board not found");
        }
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit status tables list");
        }
        if (removedTablesList == null) {
            throw new IllegalRequestContentException("Invalid status table list");
        }
        statusTableUtil.removeStatusTables(boardId, removedTablesList);
    }

    public void updateTablesOrder(User authenticatedUser, Long boardId, Map<Long, Integer> statusTableMap) {
        if (!repository.existsById(boardId)) {
            throw new RecordNotFound("Board not found");
        }
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit status tables list");
        }
        if (statusTableMap == null) {
            throw new IllegalRequestContentException("Invalid status table list");
        }
        statusTableUtil.updateStatusTablesOrder(boardId, statusTableMap);
    }

    public void rename(User authenticatedUser, Long boardId, String name) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to rename this board");
        }
        board.setName(name);
        repository.save(board);
    }

    public List<BoardSimpleDto> list(User user) {
        List<Board> boardList = repository.findAllByUserId(user.getId());
        return boardList.stream().map(boardMapper::mapToBoardSimpleDto).toList();
    }

    public BoardDataResponse findData(Long boardId, User authenticatedUser) {
        if (!boardPermissionUtil.userHasAccess(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to view this board");
        }
        Board board = repository.findById(boardId).orElseThrow(() -> new RecordNotFound("Board not found"));
        return boardMapper.mapToBoardDataDto(board, authenticatedUser.getId());
    }

    public BoardUpdateResponse update(BoardUpdateRequest request, User authenticatedUser) {
        if (!boardPermissionUtil.userIsCreator(authenticatedUser.getId(), request.getBoardId())) {
            throw new OperationNotPermittedException("User does not have the rights to view this board");
        }
        Board board = repository.findById(request.getBoardId())
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        statusTableUtil.updateStatusTables(board, request.getAdditionalStatusList());
        boardPermissionUtil.updatePermissions(board, request.getUserList());
        board.setName(request.getName());
        repository.save(board);
        return BoardUpdateResponse.builder()
                .boardId(board.getId())
                .name(board.getName())
                .build();
    }
}
