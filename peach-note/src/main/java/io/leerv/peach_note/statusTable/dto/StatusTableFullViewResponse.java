package io.leerv.peach_note.statusTable.dto;

import io.leerv.peach_note.task.dto.ExpandedTaskDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusTableFullViewResponse {
    private Long tableId;
    private List<ExpandedTaskDto> taskList;
}
