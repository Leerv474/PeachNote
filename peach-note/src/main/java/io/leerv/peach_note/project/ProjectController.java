package io.leerv.peach_note.project;

import io.leerv.peach_note.project.dto.*;
import io.leerv.peach_note.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {
    private final ProjectService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @Valid @RequestBody ProjectCreateRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.create(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete/{projectId}")
    public ResponseEntity<?> delete(
            @PathVariable Long projectId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.delete(user, projectId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(
            @Valid @RequestBody ProjectEditRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.edit(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/task_list/{projectId}")
    public ResponseEntity<ProjectTaskListResponse> getTaskList(
            @PathVariable Long projectId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.getTaskList(user, projectId)
        );
    }

    @GetMapping("/progress/{projectId}")
    public ResponseEntity<ProjectProgressResponse> getProgress(
            @PathVariable Long projectId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.getProgress(user, projectId)
        );
    }

    @GetMapping("/view/{projectId}")
    public ResponseEntity<ProjectDto> view(
            @PathVariable Long projectId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.view(user, projectId)
        );
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<List<ProjectItemDto>> list(
            @PathVariable Long boardId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.listAllByBoard(boardId, user)
        );
    }
}
