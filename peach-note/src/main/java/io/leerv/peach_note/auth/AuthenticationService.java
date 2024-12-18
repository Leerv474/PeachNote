package io.leerv.peach_note.auth;

import io.leerv.peach_note.activationToken.ActivationTokenService;
import io.leerv.peach_note.auth.dto.LoginRequest;
import io.leerv.peach_note.auth.dto.LoginResponseDto;
import io.leerv.peach_note.auth.dto.RegistrationRequest;
import io.leerv.peach_note.auth.email.EmailService;
import io.leerv.peach_note.authorities.Role;
import io.leerv.peach_note.authorities.RoleRepository;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.exceptions.OperationNotPermittedException;
import io.leerv.peach_note.security.JwtTokenService;
import io.leerv.peach_note.security.RefreshTokenRepository;
import io.leerv.peach_note.user.User;
import io.leerv.peach_note.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void register(RegistrationRequest request) throws MessagingException {
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new InternalError("Authorities were not initialized"));
        boolean emailOccupied = userRepository.existsByEmail(request.getEmail());
        if (emailOccupied) {
            throw new IllegalRequestContentException("user with such email already exists");
        }
        boolean usernameOccupied = userRepository.existsByUsername(request.getUsername());
        if (usernameOccupied) {
            throw new IllegalRequestContentException("user with such username already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountEnabled(false)
                .accountLocked(false)
                .roles(List.of(role))
                .build();
        userRepository.save(user);
        emailService.sendValidationEmail(user);
    }

    @Transactional
    public void activateAccountWithUrl(String url) {
        User user = activationTokenService.extractUserAndValidateUrl(url);
        user.setAccountEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void activateAccountWithToken(String token) {
        User user = activationTokenService.extractUserAndValidateToken(token);
        user.setAccountEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public LoginResponseDto login(@Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return LoginResponseDto.builder()
                .userId(user.getId())
                .username(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void resendActivationCode(String email) throws MessagingException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.isAccountEnabled()) {
            throw new OperationNotPermittedException("Account has been activated");
        }
        if (user.isAccountLocked()) {
            throw new OperationNotPermittedException("Account is locked");
        }
        emailService.sendValidationEmail(user);
    }

    public Map<String, String> updateTokens(String refreshToken) {
        jwtTokenService.validateRefreshToken(refreshToken);
        String username = jwtTokenService.extractUsername(refreshToken);
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found, refresh token invalid"));
        String newAccessToken = jwtTokenService.generateAccessToken(user);
        String newRefreshToken = jwtTokenService.generateRefreshToken(user);
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        jwtTokenService.invalidateRefreshToken(refreshToken);
    }
}
