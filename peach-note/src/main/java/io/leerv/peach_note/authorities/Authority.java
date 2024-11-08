package io.leerv.peach_note.authorities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {
    @Id
    private String id;

    @Override
    public String getAuthority() {
        return this.id;
    }
}
