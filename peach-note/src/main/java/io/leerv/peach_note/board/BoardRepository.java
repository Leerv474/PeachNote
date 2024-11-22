package io.leerv.peach_note.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("""
            select board
            from Board board
            join board.boardPermissionList permission
            where permission.user.id = :userId
            """)
    List<Board> findAllByUserId(Long userId);
}
