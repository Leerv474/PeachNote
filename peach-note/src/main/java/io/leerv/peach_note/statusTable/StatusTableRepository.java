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
}
