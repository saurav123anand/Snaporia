package com.snaporia.authService.controller;

import com.snaporia.authService.dto.*;
import com.snaporia.authService.entity.User;
import com.snaporia.authService.enums.Role;
import com.snaporia.authService.exception.ResourceAlreadyExistsException;
import com.snaporia.authService.exception.TokenExpiredException;
import com.snaporia.authService.service.RefreshTokenService;
import com.snaporia.authService.service.UserService;
import com.snaporia.authService.service.impl.CustomUserDetailsServiceImpl;
import com.snaporia.authService.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/public")
@Validated
public class PublicAuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterUserRequest request) throws ResourceAlreadyExistsException {
        request.setRoles(Set.of(Role.USER));
        RegisterResponse registerResponse = userService.registerUser(request);
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody AuthRequest request, HttpServletRequest httpRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        User user = (User) userDetailsService
                .loadUserByUsername(request.getUsername());

        String userAgent = httpRequest.getHeader("User-Agent");
        String ip = httpRequest.getRemoteAddr();
        String device = "UNKNOWN";

        return new AuthResponse(
                jwtUtil.generateToken(user),
                refreshTokenService.create(user.getUsername(),device,
                        ip,
                        userAgent),
                900
        );
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(
            @Valid @RequestBody RefreshTokenRequest request) throws TokenExpiredException {

        RefreshTokenSession session =
                refreshTokenService.validateAndGetUserEmail(
                        request.getRefreshToken());

        refreshTokenService.delete(request.getRefreshToken());
        User user =
                (User)userDetailsService.loadUserByUsername(session.getUserEmail());

        String newAccessToken = jwtUtil.generateToken(user);

        String newRefreshToken =
                refreshTokenService.create(
                        session.getUserEmail(),
                        session.getDevice(),
                        session.getIp(),
                        session.getUserAgent()
                );

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                900
        );
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String refreshToken) {
        refreshTokenService.delete(refreshToken);
    }

}
