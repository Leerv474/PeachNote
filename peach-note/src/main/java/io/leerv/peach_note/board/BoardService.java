package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.BoardCreateRequest;
import io.leerv.peach_note.board.dto.BoardCreateResponse;
import io.leerv.peach_note.board.dto.BoardDto;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.permission.BoardPermissionService;
import io.leerv.peach_note.project.ProjectService;
import io.leerv.peach_note.statusTable.StatusTableService;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardPermissionService boardPermissionService;
    private final StatusTableService statusTableService;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public BoardCreateResponse create(BoardCreateRequest request, User authenticatedUser) {
        Board board = Board.builder()
                .name(request.getName())
                .build();

        boardRepository.save(board);
        boardPermissionService.grantCreatorPermissions(authenticatedUser, board);
        statusTableService.createStatusTables(board, request.getAdditionalStatusMap());

        return BoardMapper.mapToBoardCreateResponse(board);
    }

    public BoardDto find(Long boardId, User authenticatedUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionService.userHasAccess(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to view this board");
        }
        return BoardMapper.mapToBoardDto(board);
    }

    public void delete(Long boardId, User authenticatedUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to delete this board");
        }
        statusTableService.deleteAllTables(board.getStatusTableList());
        projectService.deleteAllProjects(board.getProjectList());
        boardRepository.delete(board);
    }

    @Transactional(rollbackOn = {
            IllegalRequestContentException.class,
            UsernameNotFoundException.class
    })
    public void addUsers(User authenticatedUser, Long boardId, Map<Long, String> usersPermissions) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
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
                boardPermissionService.grantCreatorPermissions(user, board);
            }
            if (permissionIsViewer) {
                boardPermissionService.grantViewerPermissions(user, board);
            }
        }
    }

    @Transactional(rollbackOn = {
            UsernameNotFoundException.class
    })
    public void removeUsers(User authenticatedUser, Long boardId, List<Long> userIdList) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit user list");
        }
        if (userIdList == null) {
            throw new IllegalRequestContentException("Invalid users list");
        }
        for (var userId : userIdList) {
            if (!userRepository.existsById(userId)) {
                throw new UsernameNotFoundException("User not found");
            }
            userRepository.deleteById(userId);
        }
    }

    @Transactional(rollbackOn = {
            IllegalRequestContentException.class,
            RecordNotFound.class
    })
    public void changeUserPermissions(User authenticatedUser, Long boardId, Map<Long, String> usersPermissions) {
        if (!boardRepository.existsById(boardId)) {
            throw new RecordNotFound("Board not found");
        }
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
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
                boardPermissionService.setEditorPermission(userId, boardId);
            }
            if (permissionIsViewer) {
                boardPermissionService.setViewerPermission(userId, boardId);
            }
        }
    }

    public void addTables(User authenticatedUser, Long boardId, Map<String, Integer> statusTableMap) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board Not Found"));
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit status tables list");
        }
        if (statusTableMap == null) {
            throw new IllegalRequestContentException("Invalid status table list");
        }
        statusTableService.addStatusTables(board, statusTableMap);
    }

    public void removeTables(User authenticatedUser, Long boardId, List<Long> removedTablesList) {
        if (!boardRepository.existsById(boardId)) {
            throw new RecordNotFound("Board not found");
        }
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit status tables list");
        }
        if (removedTablesList == null) {
            throw new IllegalRequestContentException("Invalid status table list");
        }
        statusTableService.removeStatusTables(boardId, removedTablesList);
    }

    public void updateTablesOrder(User authenticatedUser, Long boardId, Map<Long, Integer> statusTableMap) {
        if (!boardRepository.existsById(boardId)) {
            throw new RecordNotFound("Board not found");
        }
        if (!boardPermissionService.userIsCreator(authenticatedUser.getId(), boardId)) {
            throw new OperationNotPermittedException("User does not have the rights to edit status tables list");
        }
        if (statusTableMap == null) {
            throw new IllegalRequestContentException("Invalid status table list");
        }
        statusTableService.updateStatusTablesOrder(boardId, statusTableMap);
    }
}
