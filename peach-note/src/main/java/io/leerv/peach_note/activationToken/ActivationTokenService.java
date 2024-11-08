package io.leerv.peach_note.activationToken;

import io.leerv.peach_note.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationTokenService {
    private final ActivationTokenRepository repository;

    @Value("${application.mailing.activation-token-length}")
    private long activationTokenLength;

    public void saveActivationToken(String token, String url, User user) {
        ActivationToken activationToken = ActivationToken.builder()
                .token(token)
                .url(url)
                .user(user)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        repository.save(activationToken);
    }

    public String generateActivationToken() {
        String buildSet = "1234567890";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < activationTokenLength; i++) {
            SecureRandom random = new SecureRandom();
            builder.append(buildSet.charAt(random.nextInt(buildSet.length())));
        }
        return builder.toString();
    }

    public User extractUserAndValidateUrl(String url) {
        ActivationToken activationToken = repository.findByUrl(url)
                .orElseThrow(() -> new IllegalStateException("Activation token not found"));
        if (!isTokenValid(activationToken)) {
            throw new IllegalStateException("Activation token expired");
        }
        return activationToken.getUser();
    }

    public User extractUserAndValidateToken(String token) {
        ActivationToken activationToken = repository.findByUrl(token)
                .orElseThrow(() -> new IllegalStateException("Activation token not found"));
        if (!isTokenValid(activationToken)) {
            throw new IllegalStateException("Activation token expired");
        }
        return activationToken.getUser();
    }

    private boolean isTokenValid(ActivationToken token) {
        return !token.isRevoked() && token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public String generateActivationUrl() {
        return UUID.randomUUID().toString();
    }

}
