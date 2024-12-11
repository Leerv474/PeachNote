package io.leerv.peach_note.user;

import io.leerv.peach_note.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService service;

    @GetMapping("/get_username")
    public ResponseEntity<SimpleUserDto> getUsername(
            Authentication authentication
    ) {
        return ResponseEntity.ok(service.getUsername(authentication
        ));
    }

    @PostMapping("/rename")
    public ResponseEntity<?> renameUser(
            @Valid @RequestBody UserRenameRequest request,
            Authentication authentication
    ) {
        Map<String, String> newJwtPair = service.rename(authentication, request.getUsername());
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                newJwtPair.get("refreshToken"), 30 * 24 * 60 * 60, "/"
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", newJwtPair.get("accessToken"));
        httpHeaders.set(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody UserChangePasswordRequest request,
            Authentication authentication
    ) {
        Map<String, String> newJwtPair = service.changePassword(authentication, request.getPassword(), request.getNewPassword());
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                newJwtPair.get("refreshToken"), 30 * 24 * 60 * 60, "/"
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", newJwtPair.get("accessToken"));
        httpHeaders.set(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(
            @Valid @RequestBody UserChangeEmailRequest request,
            Authentication authentication
    ) {
        Map<String, String> newJwtPair = service.changeEmail(authentication, request.getEmail());
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                newJwtPair.get("refreshToken"), 30 * 24 * 60 * 60, "/"
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", newJwtPair.get("accessToken"));
        httpHeaders.set(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    public ResponseEntity<?> delete(
            @Valid @RequestBody UserDeleteRequest request,
            Authentication authentication
    ) {
        service.deleteUser(authentication, request);
        return ResponseEntity.ok().build();
    }
}
