package io.leerv.peach_note.board.permission;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.user.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "board_permissions")
public class BoardPermission {
    @EmbeddedId
    @Column(insertable = false, updatable = false)
    private BoardPermissionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @OneToOne
    @JoinColumn(name = "permission_name", referencedColumnName = "name")
    private Permission permission;
}
