package io.leerv.peach_note.board.permission;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BoardPermissionId implements Serializable {
    private Long userId;
    private Long boardId;
}
