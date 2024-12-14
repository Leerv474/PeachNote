package io.leerv.peach_note.statusTable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatusTableRepository extends JpaRepository<StatusTable, Long>  {
    @Query("""
            select st
            from StatusTable st
            where st.board.id = :boardId
            and st.name = 'Bucket'
            """)
    Optional<StatusTable> findBucketByBoardId(Long boardId);

    @Query("""
           select st
           from StatusTable st
           where st.board.id = :boardId
           """)
    List<StatusTable> findAllByBoardId(Long boardId);

    @Query("""
            select st
            from StatusTable st
            where st.board.id = :boardId
            and st.displayOrder = :displayOrder
            """)
    Optional<StatusTable> findByDisplayOrderAndBoardId(Integer displayOrder, Long boardId);

    @Query("""
            select case when count(st) > 0 then true else false end
            from StatusTable st
            where st.name = :name
            and st.board.id = :boardId
            """)
    boolean existsByNameAndBoardId(String name, Long boardId);

    @Query("""
            select st
            from StatusTable st
            where st.board.id = :boardId
            and st.name = 'Await'
            """)
    Optional<StatusTable> findAwaitByBoardId(Long boardId);

    @Query("""
            select st
            from StatusTable st
            where st.board.id = :boardId
            and st.name = 'Delayed'
            """)
    Optional<StatusTable> findDelayedByBoardId(Long boardId);

    @Query("""
            select st
            from StatusTable st
            where st.board.id = :boardId
            and st.name = 'Current'
            """)
    Optional<StatusTable> findCurrentByBoardId(Long boardId);

    @Query("""
            select st
            from StatusTable st
            where st.board.id = :boardId
            and st.displayOrder = 4
            """)
    Optional<StatusTable> findFirstCompletionStatus(Long boardId);
}
