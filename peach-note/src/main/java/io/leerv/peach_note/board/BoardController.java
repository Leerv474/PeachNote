package io.leerv.peach_note.board;

import io.leerv.peach_note.board.dto.*;
import io.leerv.peach_note.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public ResponseEntity<List<BoardSimpleDto>> list(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.list(user)
        );
    }

    @GetMapping("/view/{boardId}")
    public ResponseEntity<BoardDto> view(
            @PathVariable Long boardId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.find(boardId, user)
        );
    }

    @GetMapping("/view_data/{boardId}")
    public ResponseEntity<BoardDataResponse> viewData(
            @PathVariable Long boardId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.findData(boardId, user)
        );
    }

    @GetMapping("/delete/{boardId}")
    public ResponseEntity<?> delete(
            @PathVariable Long boardId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.delete(boardId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<BoardUpdateResponse> update(
            @Valid @RequestBody BoardUpdateRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(service.update(request, user));
    }

}
