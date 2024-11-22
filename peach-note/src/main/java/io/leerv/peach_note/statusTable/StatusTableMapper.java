package io.leerv.peach_note.statusTable;


import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;

public class StatusTableMapper {
    public static StatusTableItemDto mapToStatusTableItemDto(StatusTable statusTable) {
        return StatusTableItemDto.builder()
                .tableId(statusTable.getId())
                .name(statusTable.getName())
                .displayOrder(statusTable.getDisplayOrder())
                .build();
    }
}
