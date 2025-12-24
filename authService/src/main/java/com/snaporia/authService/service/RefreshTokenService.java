package com.snaporia.authService.service;

import com.snaporia.authService.dto.RefreshTokenSession;
import com.snaporia.authService.exception.TokenExpiredException;

public interface RefreshTokenService {
    String create(String userEmail,String device, String ip, String userAgent);
    RefreshTokenSession validateAndGetUserEmail(String refreshToken) throws TokenExpiredException;
    void delete(String refreshToken);
    void logoutAllDevices(String userEmail);
}
