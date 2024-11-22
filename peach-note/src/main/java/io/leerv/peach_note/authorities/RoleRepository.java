package io.leerv.peach_note.authorities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);

    @Query("""
            select role
            from Role role
            where role.user.id = :userId
            """)
    List<Role> findByUserId(Long userId);
}
