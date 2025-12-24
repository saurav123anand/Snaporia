package com.snaporia.authService.dto;

import com.snaporia.authService.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 15, message = "Last name must be between 2 and 15 characters")
    private String lastName;
    private Set<Role> roles;
    private String profilePicture;
    private String bio;
}
