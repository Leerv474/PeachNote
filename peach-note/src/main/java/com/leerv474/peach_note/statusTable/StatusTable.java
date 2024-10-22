package com.leerv474.peach_note.statusTable;

import com.leerv474.peach_note.project.Project;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "status_tables")
public class StatusTable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @OneToMany(mappedBy = "statusTable")
    private List<Project> projects;
}
