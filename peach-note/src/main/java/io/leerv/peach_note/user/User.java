package io.leerv.peach_note.user;

import io.leerv.peach_note.activationToken.ActivationToken;
import io.leerv.peach_note.authorities.Role;
import io.leerv.peach_note.board.permission.BoardPermission;
import io.leerv.peach_note.security.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private boolean accountEnabled;
    private boolean accountLocked;
    private String telegram_tag;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<RefreshToken> refreshTokenList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BoardPermission> boardPermissionList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ActivationToken> token;

    @Override
    public String getName() {
        return email;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled;
    }
}
