package io.leerv.peach_note.task;

import io.leerv.peach_note.task.dto.TaskCreateDto;
import io.leerv.peach_note.task.dto.TaskDto;
import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {
    private final TaskService service;

    public ResponseEntity<TaskDto> create(
            @RequestBody TaskCreateDto request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.create(request, user)
        );
    }
}
