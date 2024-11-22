package io.leerv.peach_note.statusTable;

import io.leerv.peach_note.statusTable.dto.StatusTableFullViewResponse;
import io.leerv.peach_note.statusTable.dto.StatusTableRenameRequest;
import io.leerv.peach_note.statusTable.dto.StatusTableSimpleViewResponse;
import io.leerv.peach_note.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/simpleView/{tableId}")
    public ResponseEntity<StatusTableSimpleViewResponse> simpleView(
            @PathVariable Long tableId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.simpleView(user, tableId)
        );
    }

    @GetMapping("/fullView/{tableId}")
    public ResponseEntity<StatusTableFullViewResponse> fullView(
            @PathVariable Long tableId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.fullView(user, tableId)
        );
    }
}