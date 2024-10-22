package com.leerv474.peach_note.board;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class BoardPermissionKey implements Serializable {

    private Long userId;
    private Long boardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPermissionKey that = (BoardPermissionKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(boardId, that.boardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, boardId);
    }
}
