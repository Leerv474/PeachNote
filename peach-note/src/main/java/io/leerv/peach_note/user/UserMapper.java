package io.leerv.peach_note.user;

import io.leerv.peach_note.user.dto.SimpleUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public SimpleUserDto mapToSimpleUserDto(User user) {
        return SimpleUserDto.builder()
                .userId(user.getId())
                .username(user.getName())
                .build();
    }
}
