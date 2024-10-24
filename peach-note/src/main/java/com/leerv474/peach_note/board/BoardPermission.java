package com.leerv474.peach_note.board;


import com.leerv474.peach_note.permission.Permission;
import com.leerv474.peach_note.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "board_permissions")
public class BoardPermission {

    @EmbeddedId
    private BoardPermissionKey id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId("board_id")
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @OneToOne
    @JoinColumn(name = "permission_name", referencedColumnName = "name")  // Reference role by name
    private Permission permission;
}
