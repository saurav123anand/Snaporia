package com.snaporia.authService.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenSession {

    private String userEmail;
    private String device;
    private String ip;
    private String userAgent;
    private LocalDateTime loginAt;
}