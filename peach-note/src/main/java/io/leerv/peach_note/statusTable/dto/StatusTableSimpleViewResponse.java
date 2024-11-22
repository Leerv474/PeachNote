package io.leerv.peach_note.statusTable.dto;

import io.leerv.peach_note.task.dto.SimpleTaskDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusTableSimpleViewResponse {
    private Long tableId;
    private List<SimpleTaskDto> taskList;
}
