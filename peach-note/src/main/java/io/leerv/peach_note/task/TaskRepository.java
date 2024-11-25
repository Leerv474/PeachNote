package io.leerv.peach_note.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
            select case when count(project) > 0 then true else false end
            from Project project
            where title = :title
            and board.id = :boardId
            """)
    boolean isUniqueTitleInBoard(String title, Long boardId);
}
