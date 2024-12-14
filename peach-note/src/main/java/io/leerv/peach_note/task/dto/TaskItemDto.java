package io.leerv.peach_note.task.dto;

import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskItemDto {
    private Long taskId;
    private LocalDate deadline;
    private StatusTableItemDto statusTable;
    private String title;
    private Long priority;
}
