package io.leerv.peach_note.statusTable.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalTablesDto {
    private Long tableId;
    private String name;
}
