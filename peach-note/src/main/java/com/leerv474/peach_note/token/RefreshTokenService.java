package com.leerv474.peach_note.token;

import com.leerv474.peach_note.user.User;
import com.leerv474.peach_note.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private final UserRepository userRepository;

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtRefreshExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long jwtRefreshExpiration) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String newToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();

        saveToken(claims, userDetails, newToken);
        return newToken;
    }

    private void saveToken(Map<String, Object> claims, UserDetails userDetails, String token) {
        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .owner(owner)
                .sessionId(claims.get("sessionId").toString())
                .tokenHash(token)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    // validation
    public boolean validateToken(String token) {
        Claims claims = extractClaims(token);
        return isTokenRevoked(token) && isTokenExpired(claims);
    }

    private boolean isTokenRevoked(String token) {
        // todo: define a custom exception
        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(token).orElseThrow(() -> new RuntimeException("Token not found"));
        return storedToken.isRevoked();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public String extractSessionId(String token) {
        Claims claims = extractClaims(token);
        return claims.get("sessionId").toString();
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
