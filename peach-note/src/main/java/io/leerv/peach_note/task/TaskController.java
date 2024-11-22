package io.leerv.peach_note.task;

import io.leerv.peach_note.task.dto.TaskChangeStatusRequest;
import io.leerv.peach_note.task.dto.TaskCreateRequest;
import io.leerv.peach_note.task.dto.TaskDto;
import io.leerv.peach_note.task.dto.TaskEditRequest;
import io.leerv.peach_note.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {
    private final TaskService service;

    @PostMapping("/create")
    public ResponseEntity<TaskDto> create(
            @Valid @RequestBody TaskCreateRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.create(request, user)
        );
    }

    @GetMapping("/view/{taskId}")
    public ResponseEntity<TaskDto> view(
            @Valid @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                service.find(user, taskId)
        );
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(
            @Valid @RequestBody TaskEditRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.edit(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/convertToProject/{taskId}")
    public ResponseEntity<?> convertToProject(
            @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.convertToProject(user, taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/promoteStatus/{taskId}")
    public ResponseEntity<?> promoteStatus(
            @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.promoteStatus(user, taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(
            @Valid @RequestBody TaskChangeStatusRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.changeStatus(user, request.getTaskId(), request.getStatusTableId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete/{taskId}")
    public ResponseEntity<?> delete(
            @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        service.delete(user, taskId);
        return ResponseEntity.ok().build();
    }

}
