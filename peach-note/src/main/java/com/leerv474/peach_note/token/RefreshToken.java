package com.leerv474.peach_note.token;

import com.leerv474.peach_note.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
    @NotEmpty
    @Column(length = 512, unique = true, nullable = false)
    private String tokenHash;
    @NotNull
    @NotEmpty
    @Column(length = 512)
    private String sessionId;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
}
