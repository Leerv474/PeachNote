package io.leerv.peach_note.task.dto;

import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskItemDto {
    private Long taskId;
    private StatusTableItemDto statusTable;
    private String title;
    private Long priority;
}
