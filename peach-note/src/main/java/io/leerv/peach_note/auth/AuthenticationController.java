package io.leerv.peach_note.auth;

import io.leerv.peach_note.auth.dto.LoginRequest;
import io.leerv.peach_note.auth.dto.LoginResponse;
import io.leerv.peach_note.auth.dto.LoginResponseDto;
import io.leerv.peach_note.auth.dto.RegistrationRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) throws MessagingException {
        service.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activate_account")
    public ResponseEntity<?> activateAccountWithToken(@RequestParam String token) {
        service.activateAccountWithToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activateUrl/{url}")
    public ResponseEntity<?> activateAccount(@PathVariable String url) {
        service.activateAccountWithUrl(url);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resend_activation_code")
    public ResponseEntity<?> resendActivationCode(@RequestParam String email) throws MessagingException {
        service.resendActivationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponseDto loginDto = service.login(request);
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                loginDto.getRefreshToken(), 30 * 24 * 60 * 60, "/"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", loginDto.getAccessToken());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(headers).body(
                LoginResponse.builder()
                        .userId(loginDto.getUserId())
                        .username(loginDto.getUsername())
                        .build()
        );
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue("refresh_token") String refreshToken
    ) {
        service.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/update_tokens")
    public ResponseEntity<?> updateTokens(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        Map<String, String> jwtTokens = service.updateTokens(refreshToken);
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                jwtTokens.get("refreshToken"), 30 * 24 * 60 * 60, "/"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtTokens.get("accessToken"));
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(headers).build();
    }
}

