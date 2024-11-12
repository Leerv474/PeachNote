package io.leerv.peach_note.auth;

import io.leerv.peach_note.auth.dto.LoginDto;
import io.leerv.peach_note.auth.dto.RegistrationDto;
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

    //TODO: proper response from the dude on youtube
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto request) throws MessagingException {
        service.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activateAccount")
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto request) {
        Map<String, String> jwtTokens = service.login(request);
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                jwtTokens.get("refreshToken"), 30*24*60*60, "/"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtTokens.get("accessToken"));
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/update_tokens")
    public ResponseEntity<?> updateTokens(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        Map<String, String> jwtTokens = service.updateTokens(refreshToken);
        String refreshTokenCookie = String.format(
                "refresh_token=%s; Max-Age=%s; Path=%s; HttpOnly",
                jwtTokens.get("refreshToken"), 30*24*60*60, "/"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtTokens.get("accessToken"));
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie);
        return ResponseEntity.ok().headers(headers).build();
    }
}

