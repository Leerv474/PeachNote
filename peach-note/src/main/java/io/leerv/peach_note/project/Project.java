package io.leerv.peach_note.project;

import io.leerv.peach_note.statusTable.StatusTable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
}
