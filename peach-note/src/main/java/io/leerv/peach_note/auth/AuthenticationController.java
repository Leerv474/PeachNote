package io.leerv.peach_note.auth;

import io.leerv.peach_note.auth.dto.LoginDto;
import io.leerv.peach_note.auth.dto.RegistrationDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //TODO: not sure if this works
    @GetMapping("/activateAccount")
    public ResponseEntity<?> activateAccountWithToken(@RequestParam String token) {
        service.activateAccountWithToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activateAccount/{url}")
    public ResponseEntity<?> activateAccount(@PathVariable String url) {
        service.activateAccountWithUrl(url);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto request) {
        //TODO: authorization
        return null;
    }
}

