package com.snaporia.authService.service;

import com.snaporia.authService.dto.RegisterUserRequest;
import com.snaporia.authService.dto.RegisterResponse;
import com.snaporia.authService.exception.ResourceAlreadyExistsException;

public interface UserService {
    RegisterResponse registerUser(RegisterUserRequest request) throws ResourceAlreadyExistsException;
}
