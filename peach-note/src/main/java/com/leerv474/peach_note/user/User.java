package com.leerv474.peach_note.user;

import com.leerv474.peach_note.board.BoardPermission;
import com.leerv474.peach_note.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false)
    private Long id;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    @Size(min = 2, max = 127)
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "BOOLEAN DEFAULT true", nullable = false)
    private Boolean accountActive;
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private Boolean accountConfirmed;
    @NotEmpty
    private String telegramTag;

    @OneToMany(mappedBy = "owner")
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "owner")
    private List<ActivationCode> activationTokens;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "permission")
    private List<BoardPermission> boardPermission;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountActive;
    }

    @Override
    public boolean isEnabled() {
        return this.accountConfirmed;
    }
}
