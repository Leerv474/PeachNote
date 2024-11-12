package io.leerv.peach_note.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    public ResponseEntity<?> create(
            @Valid @RequestBody BoardDto,
            Authentication authentication
    ) {

    }
}
