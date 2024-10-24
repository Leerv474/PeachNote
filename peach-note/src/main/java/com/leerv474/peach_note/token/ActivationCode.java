package com.leerv474.peach_note.token;

import com.leerv474.peach_note.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activation_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationCode {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
}
