package com.example.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.library.dto.LoginRequest;
import com.example.library.dto.RegisterRequest;
import com.example.library.config.CustomUserDetailsService;
import com.example.library.entity.AppUser;
import com.example.library.exceptions.DuplicateEmailException;
import com.example.library.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository repository;
    @Mock AuthenticationManager authenticationManager;

    @Test
    void normalizesEmailAndStoresOnlyPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserService service = new UserService(repository, encoder, authenticationManager);
        service.register(new RegisterRequest(" Test@Example.Com ", "mypassword"));

        ArgumentCaptor<AppUser> saved = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).saveAndFlush(saved.capture());
        assertEquals("test@example.com", saved.getValue().getEmail());
        assertTrue(encoder.matches("mypassword", saved.getValue().getPassword()));
        assertTrue(!"mypassword".equals(saved.getValue().getPassword()));
    }

    @Test
    void rejectsExistingEmail() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(new AppUser()));
        UserService service = new UserService(repository, new BCryptPasswordEncoder(), authenticationManager);
        assertThrows(DuplicateEmailException.class,
                () -> service.register(new RegisterRequest("TEST@example.com", "mypassword")));
    }

    @Test
    void mapsUniqueConstraintRaceToDuplicateEmail() {
        when(repository.saveAndFlush(any(AppUser.class)))
                .thenThrow(new DataIntegrityViolationException("unique constraint"));
        UserService service = new UserService(repository, new BCryptPasswordEncoder(), authenticationManager);
        assertThrows(DuplicateEmailException.class,
                () -> service.register(new RegisterRequest("test@example.com", "mypassword")));
    }

    @Test
    void authenticatesWithNormalizedEmail() {
        UserService service = new UserService(repository, new BCryptPasswordEncoder(), authenticationManager);
        assertEquals("test@example.com", service.login(new LoginRequest(" Test@Example.Com ", "mypassword")));
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("test@example.com", "mypassword"));
    }

    @Test
    void realAuthenticationChecksBcryptPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AppUser user = new AppUser();
        user.setEmail("test@example.com");
        user.setPassword(encoder.encode("mypassword"));
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(encoder);
        provider.setUserDetailsService(new CustomUserDetailsService(repository));
        UserService service = new UserService(repository, encoder, new ProviderManager(provider));

        assertEquals("test@example.com", service.login(new LoginRequest("test@example.com", "mypassword")));
        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest("test@example.com", "wrongpassword")));
    }
}
