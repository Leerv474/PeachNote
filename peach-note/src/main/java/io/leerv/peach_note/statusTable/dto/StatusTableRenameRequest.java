package io.leerv.peach_note.statusTable.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusTableRenameRequest {
    private Long tableId;
    private String name;
}
