package io.leerv.peach_note.board.permission;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    private String name;
    private String description;
    private Integer permissionLevel;
    private boolean roleActive;

    @OneToOne(mappedBy = "permission")
    private BoardPermission boardPermission;
}
