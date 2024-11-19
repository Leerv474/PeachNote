package io.leerv.peach_note.statusTable;

import io.leerv.peach_note.statusTable.dto.StatusTableRenameRequest;
import io.leerv.peach_note.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statusTable")
public class StatusTableController {
    private final StatusTableService service;

    @PostMapping("/rename")
    public ResponseEntity<?> rename(
            @Valid @RequestBody StatusTableRenameRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.rename(user, request.getTableId(), request.getName());
        ResponseEntity.ok().build();
    }
}