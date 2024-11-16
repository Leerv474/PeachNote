package io.leerv.peach_note.statusTable;

import io.leerv.peach_note.board.Board;
import io.leerv.peach_note.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "status_tables")
public class StatusTable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @OneToMany(mappedBy = "statusTable")
    private List<Task> taskList;
}
