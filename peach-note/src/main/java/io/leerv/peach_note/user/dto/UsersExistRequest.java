package io.leerv.peach_note.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UsersExistRequest {
    private String someshit;
    private List<String> usernames;
}
