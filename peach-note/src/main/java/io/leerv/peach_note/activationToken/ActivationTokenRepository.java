package io.leerv.peach_note.activationToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    //TODO: queries
    Optional<ActivationToken> findByToken(String token);
    Optional<ActivationToken> findByUrl(String url);
}
