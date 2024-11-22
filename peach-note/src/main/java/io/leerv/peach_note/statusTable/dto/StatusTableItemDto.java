package io.leerv.peach_note.statusTable.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusTableItemDto {
    private Long tableId;
    private String name;
    private Integer displayOrder;
}
