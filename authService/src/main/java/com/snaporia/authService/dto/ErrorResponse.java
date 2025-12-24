package com.snaporia.authService.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;
}