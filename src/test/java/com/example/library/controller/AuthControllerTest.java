package com.example.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.library.config.JwtUtil;
import com.example.library.dto.LoginRequest;
import com.example.library.dto.RegisterRequest;
import com.example.library.exceptions.DuplicateEmailException;
import com.example.library.exceptions.GlobalExceptionHandler;
import com.example.library.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock UserService userService;
    @Mock JwtUtil jwtUtil;
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, jwtUtil))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registersWithFormData() throws Exception {
        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .param("password", "mypassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));
        verify(userService).register(new RegisterRequest("test@example.com", "mypassword"));
    }

    @Test
    void logsInWithFormData() throws Exception {
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .param("password", "mypassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
        verify(userService).login(new LoginRequest("test@example.com", "mypassword"));
    }

    @Test
    void acceptsJsonAndReturnsTokenOnLegacyEndpoint() throws Exception {
        when(userService.login(any(LoginRequest.class))).thenReturn("test@example.com");
        when(jwtUtil.generateToken("test@example.com")).thenReturn("test-token");
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"mypassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void rejectsInvalidEmailAndShortPassword() throws Exception {
        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "invalid")
                .param("password", "short"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email must be valid"))
                .andExpect(jsonPath("$.password").value("Password must be at least 6 characters"));
    }

    @Test
    void rejectsDuplicateEmail() throws Exception {
        doThrow(new DuplicateEmailException()).when(userService).register(any(RegisterRequest.class));
        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"mypassword\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already registered"));
    }

    @Test
    void rejectsWrongPassword() throws Exception {
        when(userService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("bad"));
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));
    }
}
