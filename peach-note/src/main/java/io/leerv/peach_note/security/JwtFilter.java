package io.leerv.peach_note.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.leerv.peach_note.exceptions.ExceptionDto;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getContextPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeaders = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken;
        final String email;

        if (authHeaders == null || !authHeaders.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        accessToken = authHeaders.substring(7);
        try {

            email = jwtTokenService.extractUsername(accessToken);
            if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                throw new AuthenticationException("Jwt token invalid");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException | ExpiredJwtException e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            ExceptionDto.builder()
                                    .error(e.getMessage())
                                    .build()
                    )
            );
        }
    }
}
