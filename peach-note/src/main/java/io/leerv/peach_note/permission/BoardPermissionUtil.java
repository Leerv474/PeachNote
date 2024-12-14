package io.leerv.peach_note.permission;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import io.leerv.peach_note.user.dto.UserPermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class BoardPermissionUtil {
    private final PermissionUtil permissionUtil;
    private final BoardPermissionRepository repository;
    private final UserRepository userRepository;

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
        if (repository.userPermissionExists(user.getId(), board.getId()).orElse(false)) {
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

    public void updatePermissions(Board board, List<UserPermissionDto> userList, String authenticatedUserName) {
        for (UserPermissionDto dto : userList) {
            if (dto.getUsername().equals(authenticatedUserName)) {
                continue;
            }
            if (dto.getPermissionLevel() == 3) {
                continue;
            }

            BoardPermission permission = repository.findByBoardIdAndUsername(board.getId(), dto.getUsername()).orElse(null);
            if (permission == null) {
                grantNewPermissions(board, dto);
                continue;
            }

            if (Objects.equals(dto.getPermissionLevel(), permission.getPermission().getPermissionLevel())) {
                continue;
            }
            if (dto.getPermissionLevel() == 2) {
                permission.setPermission(permissionUtil.getEditorPermission());
                repository.save(permission);
            }
            if (dto.getPermissionLevel() == 1) {
                permission.setPermission(permissionUtil.getViewerPermission());
                repository.save(permission);
            }
        }
    }

    private void grantNewPermissions(Board board, UserPermissionDto dto) {
        User user = userRepository.findUserByUsername(dto.getUsername()).orElse(null);
        if (user == null) {
            return;
        }
        if (dto.getPermissionLevel() == 2) {
            grantEditorPermissions(user, board);
        }
        if (dto.getPermissionLevel() == 1) {
            grantViewerPermissions(user, board);
        }
    }

    public Integer getUserPermission(Long boardId, Long userId) {
        BoardPermission permission = repository.findByBoardIdAndUserId(userId, boardId)
                .orElseThrow(() -> new RecordNotFound("User permission not found"));
        return permission.getPermission().getPermissionLevel();
    }
}
