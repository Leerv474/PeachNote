package com.leerv474.peach_note.task;

import com.leerv474.peach_note.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 2, max = 127)
    private String title;
    private String description;
    @CreatedDate
    @Column(updatable = false)
    private LocalDate creationDate;
    private LocalDate deadline;
    private LocalDate completionDate;
    @Min(0)
    @Max(100)
    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;
}
