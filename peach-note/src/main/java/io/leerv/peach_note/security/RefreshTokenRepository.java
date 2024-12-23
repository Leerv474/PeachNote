package io.leerv.peach_note.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("""
            select rs
            from RefreshToken rs
            where rs.token = :token
            """)
    Optional<RefreshToken> findByToken(String token);
    @Query("""
            select rs
            from RefreshToken rs
            where rs.revoked = true
            """)
    List<RefreshToken> findAllRevokedTokens();

    @Query("""
            select rs
            from RefreshToken rs
            where rs.user.id = :userId
            """)
    List<RefreshToken> findTokensByUserId(Long userId);

    @Modifying
    @Query("""
            delete from RefreshToken rs
            where rs.token = :token
            """)
    void deleteByToken(String token);
}
