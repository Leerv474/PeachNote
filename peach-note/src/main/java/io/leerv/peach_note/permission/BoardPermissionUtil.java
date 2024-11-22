package io.leerv.peach_note.permission;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class BoardPermissionUtil {
    private final PermissionUtil permissionUtil;
    private final BoardPermissionRepository repository;

    public void grantCreatorPermissions(User user, Board board) {
        grantPermission(user, board, PermissionUtil::getCreatorPermission);
    }

    public void grantEditorPermissions(User user, Board board) {
        grantPermission(user, board, PermissionUtil::getEditorPermission);
    }

    public void grantViewerPermissions(User user, Board board) {
        grantPermission(user, board, PermissionUtil::getViewerPermission);
    }

    private void grantPermission(User user, Board board, Function<PermissionUtil, Permission> permissionResolver) {
        if (repository.userPermissionExists(user.getId(), board.getId())) {
            throw new IllegalRequestContentException("Permission already exists");
        }
        BoardPermission boardPermission = BoardPermission.builder()
                .user(user)
                .board(board)
                .permission(permissionResolver.apply(permissionUtil))
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
        setPermission(userId, boardId, PermissionUtil::getViewerPermission);
    }

    public void setEditorPermission(Long userId, Long boardId) {
        setPermission(userId, boardId, PermissionUtil::getEditorPermission);
    }

    private void setPermission(Long userId, Long boardId, Function<PermissionUtil, Permission> permissionResolver) {
        BoardPermission boardPermission = repository.findByBoardIdAndUserId(userId, boardId)
                .orElseThrow(() -> new RecordNotFound("BoardPermission not found"));
        boardPermission.setPermission(permissionResolver.apply(permissionUtil));
        repository.save(boardPermission);
    }

    public void revokePermission(Long userId, Long boardId) {
        repository.deleteByUserIdAndBoardId(userId, boardId);
    }
}
