package io.leerv.peach_note.board;

import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.statusTable.StatusTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BoardUtil {
    private final BoardRepository repository;

    public boolean containStatus(Board board, Long statusTableId) {
        for (StatusTable table : board.getStatusTableList()) {
            if (Objects.equals(table.getId(), statusTableId)) {
                return true;
            }
        }
        return false;
    }

    public Board findBoardById(Long boardId) {
        return repository.findById(boardId)
                .orElseThrow(() -> new RecordNotFound("Board not found"));
    }
}
