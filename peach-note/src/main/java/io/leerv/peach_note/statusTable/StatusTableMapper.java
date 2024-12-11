package io.leerv.peach_note.statusTable;


import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;
import org.springframework.stereotype.Component;

@Component
public class StatusTableMapper {
    public StatusTableItemDto mapToStatusTableItemDto(StatusTable statusTable) {
        return StatusTableItemDto.builder()
                .tableId(statusTable.getId())
                .name(statusTable.getName())
                .displayOrder(statusTable.getDisplayOrder())
                .build();
    }
}
