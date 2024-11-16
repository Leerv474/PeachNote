package io.leerv.peach_note.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository repository;

    public Permission getCreatorPermission() {
        return repository.findById("CREATOR")
                .orElseThrow(() -> new IllegalArgumentException("Permissions were not initialized, CREATOR permission not found"));
    }

    public Permission getEditorPermission() {
        return repository.findById("EDITOR")
                .orElseThrow(() -> new IllegalArgumentException("Permissions were not initialized, EDITOR permission not found"));
    }

    public Permission getViewerPermission() {
        return repository.findById("VIEWER")
                .orElseThrow(() -> new IllegalArgumentException("Permissions were not initialized, VIEWER permission not found"));
    }
}
