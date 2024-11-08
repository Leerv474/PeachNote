package io.leerv.peach_note.board;

import io.leerv.peach_note.activationToken.ActivationToken;
import io.leerv.peach_note.board.permission.BoardPermission;
import io.leerv.peach_note.statusTable.StatusTable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "board")
    private List<BoardPermission> boardPermissionList;

    @OneToMany(mappedBy = "board")
    private List<StatusTable> statusTableList;

    @OneToMany(mappedBy = "user")
    private List<ActivationToken> activationTokenList;
}
