package io.leerv.peach_note.board.dto;

import io.leerv.peach_note.user.dto.UserPermissionDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardCreateRequest {
    @NotNull(message = "name is mandatory")
    @NotBlank(message = "name is mandatory")
    @Size(min = 2, max = 64)
    private String name;
    private List<String> additionalStatusList;
    private List<UserPermissionDto> userList;
}
