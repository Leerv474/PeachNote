package io.leerv.peach_note.task;

import io.leerv.peach_note.task.dto.TaskDto;

public class TaskMapper {
    public static TaskDto mapToTaskDto(Task task) {
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .creationDate(task.getCreationDate())
                .deadline(task.getDeadline())
                .priority(task.getPriority())
                .projectId(task.getProject() == null ? null : task.getProject().getId())
                .statusTableId(task.getStatusTable() == null ? null : task.getStatusTable().getId())
                .build();
    }
}
