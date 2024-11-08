package io.leerv.peach_note.auth;

import io.leerv.peach_note.activationToken.ActivationTokenService;
import io.leerv.peach_note.auth.dto.RegistrationDto;
import io.leerv.peach_note.auth.email.EmailService;
import io.leerv.peach_note.authorities.Authority;
import io.leerv.peach_note.authorities.AuthorityRepository;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;

    public void register(RegistrationDto request) throws MessagingException {
        Authority authority = authorityRepository.findById("USER")
                .orElseThrow(() -> new IllegalStateException("Authorities were not initialized"));
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountEnabled(false)
                .accountLocked(false)
                .authorities(List.of(authority))
                .build();
        emailService.sendValidationEmail(user);
        userRepository.save(user);
    }


    public void activateAccountWithUrl(String url) {
        User user = activationTokenService.extractUserAndValidateUrl(url);
        user.setAccountEnabled(true);
        userRepository.save(user);
    }


    public void activateAccountWithToken(String token) {
        User user = activationTokenService.extractUserAndValidateToken(token);
        user.setAccountEnabled(true);
        userRepository.save(user);
    }
}
