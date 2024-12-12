package io.leerv.peach_note.user;

import io.leerv.peach_note.authorities.RoleUtil;
import io.leerv.peach_note.exceptions.IllegalRequestContentException;
import io.leerv.peach_note.security.JwtTokenService;
import io.leerv.peach_note.user.dto.SimpleUserDto;
import io.leerv.peach_note.user.dto.UserDeleteRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtTokenService jwtTokenService;
    private final RoleUtil roleUtil;
    private final PasswordEncoder encoder;

    public Map<String, String> rename(Authentication authentication, String username) {
        User authenticatedUser = (User) authentication.getPrincipal();
        if (username.isBlank() || username.length() < 2) {
            throw new IllegalRequestContentException("Username does not meet the requirements");
        }
        User user = repository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(username);
        repository.save(user);

        Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                user,
                authentication.getCredentials(),
                authentication.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(updatedAuth);

        jwtTokenService.invalidateAllRefreshTokens(user);

        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    public Map<String, String> changePassword(Authentication authentication, String password, String newPassword) {
        User authenticatedUser = (User) authentication.getPrincipal();

        User user = repository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!encoder.matches(password, user.getPassword())) {
            throw new IllegalRequestContentException("Incorrect password");
        }
        //todo: validator
        user.setPassword(encoder.encode(newPassword));
        repository.save(user);

        Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(updatedAuth);
        jwtTokenService.invalidateAllRefreshTokens(user);

        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    public Map<String, String> changeEmail(Authentication authentication, String email) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = repository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //todo: validator
        user.setEmail(email);
        repository.save(user);

        Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(updatedAuth);
        jwtTokenService.invalidateAllRefreshTokens(user);

        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }


    @Transactional
    public void deleteUser(Authentication authentication, UserDeleteRequest request) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = repository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalRequestContentException("Incorrect password");
        }
        roleUtil.detachRoles(user.getId());
        repository.delete(user);
    }

    public SimpleUserDto getUsername(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = repository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException(("User not found")));
        return SimpleUserDto.builder()
                .userId(user.getId())
                .username(user.getName())
                .build();
    }

    public List<Long> usersExist(List<String> usernames) {
        List<User> userList = repository.findAllByUsername(usernames);
        return userList.stream().map(User::getId).toList();
    }
}
