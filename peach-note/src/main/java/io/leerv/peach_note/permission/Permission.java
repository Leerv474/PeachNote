package io.leerv.peach_note.permission;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "permission")
    private List<BoardPermission> boardPermissionList;
}
