package io.leerv.peach_note.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardPermissionRepository extends JpaRepository<BoardPermission, Long> {
    @Query("""
            select case when br.permission.permissionLevel > 2 then true else false end
            from BoardPermission br
            where br.user.id = :userId
            and br.board.id = :boardId
            """)
    boolean userIsCreatorOfBoard(Long userId, Long boardId);

    @Query("""
            select case when br.permission.permissionLevel > 1 then true else false end
            from BoardPermission br
            where br.user.id = :userId
            and br.board.id = :boardId
            """)
    boolean userIsEditorOfBoard(Long userId, Long boardId);

    @Query("""
            select case when br.permission.permissionLevel > 0 then true else false end
            from BoardPermission br
            where br.user.id = :userId
            and br.board.id = :boardId
            """)
    boolean userIsViewerOfBoard(Long userId, Long boardId);

    @Query("""
            select bp
            from BoardPermission bp
            where bp.board.id = :boardId
            """)
    List<BoardPermission> findAllByBoardId(Long boardId);

    @Query("""
            select bp
            from BoardPermission bp
            where bp.board.id = :boardId
            and bp.user.id = :userId
            """)
    Optional<BoardPermission> findByBoardIdAndUserId(Long boardId, Long userId);
}
