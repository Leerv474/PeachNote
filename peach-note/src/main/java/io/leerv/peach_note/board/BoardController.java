package io.leerv.peach_note.board;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.leerv.peach_note.board.dto.BoardCreateRequest;
import io.leerv.peach_note.board.dto.BoardCreateResponse;
import io.leerv.peach_note.board.dto.BoardDataResponse;
import io.leerv.peach_note.board.dto.BoardDto;
import io.leerv.peach_note.board.dto.BoardRenameRequest;
import io.leerv.peach_note.board.dto.BoardSimpleDto;
import io.leerv.peach_note.board.dto.BoardUpdateRequest;
import io.leerv.peach_note.board.dto.BoardUpdateResponse;
import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
    private final BoardService service;

    @PostMapping("/create")
    public ResponseEntity<BoardCreateResponse> create(
            @Valid @RequestBody BoardCreateRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        service.create(request, user));
    }

    @PostMapping("/rename")
    public ResponseEntity<?> rename(
            @Valid @RequestBody BoardRenameRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        service.rename(user, request.getBoardId(), request.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardSimpleDto>> list(
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.list(user));
    }

    @GetMapping("/view/{boardId}")
    public ResponseEntity<BoardDto> view(
            @PathVariable Long boardId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.find(boardId, user));
    }

    @GetMapping("/view_data/{boardId}")
    public ResponseEntity<BoardDataResponse> viewData(
            @PathVariable Long boardId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.findData(boardId, user));
    }

    @GetMapping("/delete/{boardId}")
    public ResponseEntity<?> delete(
            @PathVariable Long boardId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        service.delete(boardId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<BoardUpdateResponse> update(
            @Valid @RequestBody BoardUpdateRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(service.update(request, user));
    }

    public ResponseEntity<BoardUpdateRequest> updateData(
            @Valid @PathVariable BoardUpdateRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(null);
    }
}
