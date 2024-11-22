package io.leerv.peach_note.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleUtil {
    private final RoleRepository repository;

    @Transactional
    public void detachRoles(Long userId) {
        List<Role> roles = repository.findByUserId(userId);
        roles.forEach(role ->
                role.setUsers(
                        role.getUsers().stream()
                                .filter(
                                        user -> !user.getId().equals(userId)
                                ).toList()
                )
        );
        repository.saveAll(roles);
    }
}
