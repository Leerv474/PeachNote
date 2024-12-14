package io.leerv.peach_note.project.dto;

import io.leerv.peach_note.task.dto.TaskItemDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProjectDto {
    private Long projectId;
    private String title;
    private String description;
    private LocalDate deadline;
    private Integer tasksAmount;
    private Integer finishedTasksAmount;
    private List<TaskItemDto> taskList;
}
