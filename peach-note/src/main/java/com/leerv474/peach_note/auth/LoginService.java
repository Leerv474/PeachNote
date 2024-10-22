package com.leerv474.peach_note.auth;

import com.leerv474.peach_note.auth.email.EmailService;
import com.leerv474.peach_note.auth.email.EmailTemplateName;
import com.leerv474.peach_note.role.Role;
import com.leerv474.peach_note.role.RoleRepository;
import com.leerv474.peach_note.security.JwtUtils;
import com.leerv474.peach_note.security.RefreshTokenService;
import com.leerv474.peach_note.user.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ActivationCodeRepository activationCodeRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
    // email verification TODO: add to application.properties
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${application.mailing.frontend.activation-length}")
    private int activationLength;
    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    public void register(@Valid RegistrationRequest request) throws MessagingException {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE 'USER' was not initialized"));
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .accountActive(true)
                .accountConfirmed(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String activationCode = generateActivationCode(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                activationCode,
                "Account activation"
        );
    }

    private String generateActivationCode(User user) {
        String activationCode = generateActivationCode();
        ActivationCode token = ActivationCode.builder()
                .token(activationCode)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .owner(user)
                .build();
        activationCodeRepository.save(token);
        return activationCode;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < this.activationLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public LoginResponse authenticate(@Valid LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        User user = (User)auth.getPrincipal();
        claims.put("username", user.getUsername());
        claims.put("sessionId", UUID.randomUUID().toString());
        String accessToken = jwtUtils.generateAccessToken(claims, user);
        String refreshToken = refreshTokenService.generateRefreshToken(claims, user);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
