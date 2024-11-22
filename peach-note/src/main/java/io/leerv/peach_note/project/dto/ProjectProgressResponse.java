package io.leerv.peach_note.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectProgressResponse {
    private Long projectId;
    private Integer tasksAmount;
    private Integer finishedTasks;
}
