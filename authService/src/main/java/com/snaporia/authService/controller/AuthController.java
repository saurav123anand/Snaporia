package com.snaporia.authService.controller;

import com.snaporia.authService.dto.*;
import com.snaporia.authService.entity.User;
import com.snaporia.authService.exception.ResourceAlreadyExistsException;
import com.snaporia.authService.service.RefreshTokenService;
import com.snaporia.authService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<RegisterResponse> registerByAdmin(@RequestBody RegisterUserRequest registerUserRequest) throws ResourceAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("auth object is " + authentication);
        System.out.println("getAuthorities are " + authentication.getAuthorities());
        RegisterResponse userResponse = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(
            @AuthenticationPrincipal User user) {
        refreshTokenService.logoutAllDevices(user.getUsername());
        return ResponseEntity.ok().build();
    }


}
