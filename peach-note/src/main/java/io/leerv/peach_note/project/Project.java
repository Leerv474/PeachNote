package io.leerv.peach_note.project;

import io.leerv.peach_note.statusTable.StatusTable;
import io.leerv.peach_note.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "status_table_id", referencedColumnName = "id")
    private StatusTable statusTable;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> taskList;
}
