package com.leerv474.peach_note.permission;

import com.leerv474.peach_note.board.BoardPermission;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;
    private String description;
    @ColumnDefault("true")
    private Boolean permissionActive;

    @OneToOne(mappedBy = "permission")
    private BoardPermission boardPermission;
}
