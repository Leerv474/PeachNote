package com.leerv474.peach_note.auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegistrationRequest request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/activate_account")
    public void activateAccount(
            @RequestParam String activationCode
    ) throws MessagingException {
        authenticationService.activateAccount(activationCode);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<UpdateTokensResponse> updateTokens(
            @RequestBody @Valid UpdateTokensRequest request
    ) {
        return ResponseEntity.ok(authenticationService.updateTokens(request.getRefreshToken()));
    }
}
