package io.leerv.peach_note.project.dto;

import io.leerv.peach_note.task.dto.TaskItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectTaskListResponse {
    private Long projectId;
    private List<TaskItemDto> taskList;
}
