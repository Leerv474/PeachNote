package io.leerv.peach_note.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("""
            select board
            from Board board
            join board.boardPermissionList permission
            where permission.user.id = :userId
            """)
    List<Board> findAllByUserId(Long userId);

    @Query("""
            select case when count(board) > 0 then true else false end
            from Board board
            join board.boardPermissionList permission
            where permission.user.id = :userId
            and board.name = :boardName
            """)
    boolean boardNameExistsByUserId(Long userId, String boardName);
}
