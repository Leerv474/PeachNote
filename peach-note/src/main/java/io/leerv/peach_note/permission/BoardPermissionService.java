package io.leerv.peach_note.permission;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BoardPermissionService {
    private final PermissionService permissionService;
    private final BoardPermissionRepository repository;
    private final UserRepository userRepository;


    public void grantCreatorPermissions(User user, Board board) {
        grantPermission(user, board, PermissionService::getCreatorPermission);
    }

    public void grantEditorPermissions(User user, Board board) {
        grantPermission(user, board, PermissionService::getEditorPermission);
    }

    public void grantViewerPermissions(User user, Board board) {
        grantPermission(user, board, PermissionService::getViewerPermission);
    }

    private void grantPermission(User user, Board board, Function<PermissionService, Permission> permissionResolver) {
        BoardPermission boardPermission = BoardPermission.builder()
                .user(user)
                .board(board)
                .permission(permissionResolver.apply(permissionService))
                .build();
        repository.save(boardPermission);
    }

    public boolean userHasAccess(Long userId, Long boardId) {
        return repository.userIsViewerOfBoard(userId, boardId);
    }

    public boolean userIsEditor(Long userId, Long boardId) {
        return repository.userIsEditorOfBoard(userId, boardId);
    }

    public boolean userIsCreator(Long userId, Long boardId) {
        return repository.userIsCreatorOfBoard(userId, boardId);
    }

    public List<BoardPermission> getPermissionList(Long boardId) {
        return repository.findAllByBoardId(boardId);
    }

    public void setViewerPermission(Long userId, Long boardId) {
        setPermission(userId, boardId, PermissionService::getViewerPermission);
    }

    public void setEditorPermission(Long userId, Long boardId) {
        setPermission(userId, boardId, PermissionService::getEditorPermission);
    }

    private void setPermission(Long userId, Long boardId, Function<PermissionService, Permission> permissionResolver) {
        BoardPermission boardPermission = repository.findByBoardIdAndUserId(userId, boardId)
                .orElseThrow(() -> new RecordNotFound("BoardPermission not found"));
        boardPermission.setPermission(permissionResolver.apply(permissionService));
        repository.save(boardPermission);
    }
}
