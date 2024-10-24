package com.leerv474.peach_note.auth;

import com.leerv474.peach_note.role.Role;
import com.leerv474.peach_note.role.RoleRepository;
import com.leerv474.peach_note.security.AccessTokenService;
import com.leerv474.peach_note.token.ActivationCode;
import com.leerv474.peach_note.token.ActivationCodeRepository;
import com.leerv474.peach_note.token.ActivationCodeService;
import com.leerv474.peach_note.token.RefreshTokenService;
import com.leerv474.peach_note.user.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder encoder;

    private final ActivationCodeService activationCodeService;
    private final ActivationCodeRepository activationCodeRepository;

    @Transactional
    public void register(@Valid RegistrationRequest request) throws MessagingException {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE 'USER' was not initialized"));
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .accountActive(true)
                .accountConfirmed(false)
                .roles(List.of(userRole))
                .boardPermission(new ArrayList<>())
                .telegramTag(null)
                .refreshTokens(new ArrayList<>())
                .build();
        userRepository.save(user);
        activationCodeService.sendValidationEmail(user);
    }

    @Transactional
    public void activateAccount(String code) throws MessagingException {
        // TODO: make an exception
        ActivationCode savedCode = activationCodeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalIdentifierException("Activation code not found"));
        if (LocalDateTime.now().isAfter(savedCode.getExpiresAt())) {
            activationCodeService.sendValidationEmail(savedCode.getOwner());
            throw new RuntimeException("Activation code has expired");
        }

        var user = userRepository.findById(savedCode.getOwner().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setAccountConfirmed(true);
        userRepository.save(user);
        savedCode.setValidatedAt(LocalDateTime.now());
        activationCodeRepository.save(savedCode);
    }

    @Transactional
    public LoginResponse authenticate(@Valid LoginRequest request) throws Exception {
        // TODO: make exception
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var claims = new HashMap<String, Object>();
            User user = (User)authentication.getPrincipal();
            claims.put("sessionId", UUID.randomUUID().toString());
            String accessToken = accessTokenService.generateAccessToken(new HashMap<>(), user);
            String refreshToken = refreshTokenService.generateRefreshToken(claims, user);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            // Continue with the authenticated user
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            System.out.println("Invalid credentials: " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            // Handle user not found
            System.out.println("User not found: " + e.getMessage());
            System.out.println(request.getEmail());

        }
        return null;
    }

    public UpdateTokensResponse updateTokens(String refreshToken) {
        if (!refreshTokenService.validateToken(refreshToken)) {
            //TODO: handle exception
            throw new RuntimeException("Refresh token is invalid");
        }

        var claims = new HashMap<String, Object>();
        String username = refreshTokenService.extractUsername(refreshToken);
        String sessionId = refreshTokenService.extractSessionId(refreshToken);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        claims.put("sessionId", sessionId);
        String newAccessToken = accessTokenService.generateAccessToken(new HashMap<>(), user);
        String newRefreshToken = refreshTokenService.generateRefreshToken(claims, user);
        return UpdateTokensResponse
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
