package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.*;
import io.leerv.peach_note.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
    private final BoardService service;

    @PostMapping("/create")
    public ResponseEntity<BoardCreateResponse> create(
            @Valid @RequestBody BoardCreateRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        service.create(request, user)
                );
    }

    @PostMapping("/rename")
    public ResponseEntity<?> rename(
            @Valid @RequestBody BoardRenameRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.rename(user, request.getBoardId(), request.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> findBookById(
            @PathVariable Long boardId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.find(boardId, user)
        );
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteBookById(
            @RequestParam Long bookId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.delete(bookId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addUsers")
    public ResponseEntity<?> addUsers(
            @Valid @RequestBody BoardUsersPermissionsRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.addUsers(user, request.getBoardId(), request.getUsersPermissions());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeUsers")
    public ResponseEntity<?> removeUsers(
            @Valid @RequestBody BoardRemoveUsersRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.removeUsers(user, request.getBoardId(), request.getUserIdList());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeUsers")
    public ResponseEntity<?> changeUserPermissions(
            @Valid @RequestBody BoardUsersPermissionsRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.changeUserPermissions(user, request.getBoardId(), request.getUsersPermissions());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addTables")
    public ResponseEntity<?> addTables(
            @Valid @RequestBody BoardTablesMapRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.addTables(user, request.getBoardId(), request.getStatusTableMap());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeTables")
    public ResponseEntity<?> removeTables(
            @Valid @RequestBody BoardRemoveTablesRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.removeTables(user, request.getBoardId(), request.getRemovedTablesList());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateTablesOrder")
    public ResponseEntity<?> updateTablesOrder(
            @Valid @RequestBody BoardTableUpdateRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.updateTablesOrder(user, request.getBoardId(), request.getStatusTableMap());
        return ResponseEntity.ok().build();
    }

}
