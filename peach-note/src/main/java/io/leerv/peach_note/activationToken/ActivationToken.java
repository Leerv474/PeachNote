package io.leerv.peach_note.activationToken;

import io.leerv.peach_note.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "activation_tokens")
public class ActivationToken {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String token;
    @Column(unique = true, nullable = false)
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private LocalDateTime expiresAt;
    private boolean revoked;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
