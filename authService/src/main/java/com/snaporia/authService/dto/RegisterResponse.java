package com.snaporia.authService.dto;

import com.snaporia.authService.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class RegisterResponse {
    private String message;
    private String status;
    private String username;
    private Set<String> role;
    private LocalDateTime timestamp;
}
