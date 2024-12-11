package io.leerv.peach_note.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.leerv.peach_note.exceptions.RecordNotFound;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${application.security.jwt.access-expiration}")
    private long accessTokenExpiration;
    @Value("${application.security.jwt.refresh-expiration}")
    private long refreshTokenExpiration;
    @Value("${application.security.jwt.secret-key}")
    private String secret;

    public String generateAccessToken(User user) {
        return String.format(buildToken(new HashMap<>(), user, accessTokenExpiration));
    }

    public String generateRefreshToken(User user) {
        String sessionId = UUID.randomUUID().toString();
        String token = buildToken(Map.of("sessionId", sessionId), user, refreshTokenExpiration);
        RefreshToken rsToken = RefreshToken.builder()
                .token(token)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .revoked(false)
                .user(user)
                .sessionId(sessionId)
                .build();
        refreshTokenRepository.save(rsToken);
        return token;
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        var authorities = userDetails.getAuthorities();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean validateRefreshToken(String token) {
        RefreshToken rsToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RecordNotFound("Refresh Token not found"));
        boolean isExpired = rsToken.getExpiresAt().before(new Date(System.currentTimeMillis())) ;
        return isExpired && !rsToken.isRevoked();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).after(new Date(System.currentTimeMillis()));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    private <T>T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void invalidateAllRefreshTokens(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findTokensByUserId(user.getId());
        refreshTokenRepository.deleteAll(tokens);
    }

    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
