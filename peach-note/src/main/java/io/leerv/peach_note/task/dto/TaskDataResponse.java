package io.leerv.peach_note.task.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDataResponse {
    Long taskId;
    String title;
    String description;
    LocalDate deadline;
    Long projectId;
    String projectTitle;
    Integer userPermissionLevel;
}
