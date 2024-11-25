package io.leerv.peach_note.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            select case when count(project) > 0 then true else false end
            from Project project
            where title = :title
            and board.id = :boardId
            """)
    boolean isUniqueTitleInBoard(String title, Long boardId);
}
