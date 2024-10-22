package com.leerv474.peach_note.project;

import com.leerv474.peach_note.statusTable.StatusTable;
import com.leerv474.peach_note.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Size(min = 2, max = 127)
    private String title;
    private String description;
    private LocalDate deadline;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "status_table_id", referencedColumnName = "id")
    private StatusTable statusTable;
}
