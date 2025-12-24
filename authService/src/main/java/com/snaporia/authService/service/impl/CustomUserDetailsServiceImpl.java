package com.snaporia.authService.service.impl;


import com.snaporia.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with username "+username));
    }
    public UserDetails findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found"));
    }
}
