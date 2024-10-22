package com.leerv474.peach_note.board;

import com.leerv474.peach_note.statusTable.StatusTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Size(min = 2, max = 127)
    private String boardName;

    @OneToMany(mappedBy = "board")
    private List<StatusTable> statusTables;


    @OneToMany(mappedBy = "board")
    private List<BoardPermission> boardPermission;
}
