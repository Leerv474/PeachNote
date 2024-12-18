package io.leerv.peach_note.statusTable;


import org.springframework.stereotype.Component;

import io.leerv.peach_note.statusTable.dto.AdditionalTablesDto;
import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;

@Component
public class StatusTableMapper {
    public StatusTableItemDto mapToStatusTableItemDto(StatusTable statusTable) {
        return StatusTableItemDto.builder()
                .tableId(statusTable.getId())
                .name(statusTable.getName())
                .displayOrder(statusTable.getDisplayOrder())
                .build();
    }

    public AdditionalTablesDto mapToAdditionalTablesDto(StatusTable statusTable) {
        return AdditionalTablesDto.builder()
                .tableId(statusTable.getId())
                .name(statusTable.getName())
                .build();
    }
}
