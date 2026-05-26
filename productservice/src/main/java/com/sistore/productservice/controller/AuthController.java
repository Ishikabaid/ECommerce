package com.sistore.productservice.controller;

import com.sistore.productservice.dto.AuthResponse;
import com.sistore.productservice.dto.LoginRequest;
import com.sistore.productservice.dto.RegisterRequest;
import com.sistore.productservice.security.AuthUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserDetailsService authService;

    public AuthController(AuthUserDetailsService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest user) {
       return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest user) {
        return ResponseEntity.ok(authService.login(user));
    }
}
