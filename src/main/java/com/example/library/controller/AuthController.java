package com.example.library.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.config.JwtUtil;
import com.example.library.dto.LoginRequest;
import com.example.library.dto.RegisterRequest;
import com.example.library.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(path = {"/register", "/api/auth/register"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> registerForm(@Valid @ModelAttribute RegisterRequest request) {
        return register(request);
    }

    @PostMapping(path = {"/register", "/api/auth/register"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerJson(@Valid @RequestBody RegisterRequest request) {
        return register(request);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> loginForm(@Valid @ModelAttribute LoginRequest request) {
        userService.login(request);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginJson(@Valid @RequestBody LoginRequest request) {
        userService.login(request);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, String>> tokenForm(@Valid @ModelAttribute LoginRequest request) {
        return token(request);
    }

    @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> tokenJson(@Valid @RequestBody LoginRequest request) {
        return token(request);
    }

    private ResponseEntity<String> register(RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully.");
    }

    private ResponseEntity<Map<String, String>> token(LoginRequest request) {
        String email = userService.login(request);
        return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(email)));
    }
}
