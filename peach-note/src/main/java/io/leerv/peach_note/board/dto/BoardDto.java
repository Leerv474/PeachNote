package io.leerv.peach_note.board.dto;

import io.leerv.peach_note.project.dto.ProjectItemDto;
import io.leerv.peach_note.statusTable.dto.StatusTableItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardDto {
    private Long boardId;
    private String name;
    private List<Long> userIdList;
    private List<StatusTableItemDto> statusTableList;
    private List<ProjectItemDto> projectList;
}
