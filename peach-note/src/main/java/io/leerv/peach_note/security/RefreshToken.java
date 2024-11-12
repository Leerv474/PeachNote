package io.leerv.peach_note.security;

import io.leerv.peach_note.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 512, unique = true, nullable = false)
    private String token;
    private Date issuedAt;
    private Date expiresAt;
    private String sessionId;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;
}
