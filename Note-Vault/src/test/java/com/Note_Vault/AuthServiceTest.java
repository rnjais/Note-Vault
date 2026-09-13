package com.Note_Vault;

import com.Note_Vault.dto.LoginRequest;
import com.Note_Vault.dto.LoginResponse;
import com.Note_Vault.dto.RegisterRequest;
import com.Note_Vault.entity.User;
import com.Note_Vault.exception.EmailAlreadyExistsException;
import com.Note_Vault.exception.InvalidCredentialsException;
import com.Note_Vault.exception.UsernameAlreadyExistsException;
import com.Note_Vault.repository.UserRepository;
import com.Note_Vault.service.AuthService;
import com.Note_Vault.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void register_shouldSaveUser() {

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);

        AuthService authService =
                new AuthService(userRepository, passwordEncoder, jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("aryan");
        request.setEmail("aryan@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("aryan"))
                .thenReturn(false);

        when(userRepository.existsByEmail("aryan@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        User savedUser = new User(
                "aryan",
                "aryan@gmail.com",
                "encodedPassword"
        );

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = authService.register(request);

        assertEquals("aryan", result.getUsername());
        assertEquals("aryan@gmail.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);

        AuthService authService =
                new AuthService(userRepository, passwordEncoder, jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("aryan");
        request.setEmail("aryan@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("aryan"))
                .thenReturn(true);

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository).existsByUsername("aryan");
    }
    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);

        AuthService authService =
                new AuthService(userRepository, passwordEncoder, jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("aryan2");
        request.setEmail("aryan@gmail.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("aryan2"))
                .thenReturn(false);

        when(userRepository.existsByEmail("aryan@gmail.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository).existsByEmail("aryan@gmail.com");
    }
    @Test
    void login_shouldThrowException_whenPasswordIsWrong() {

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);

        AuthService authService =
                new AuthService(userRepository, passwordEncoder, jwtService);

        LoginRequest request = new LoginRequest();
        request.setUsername("aryan");
        request.setPassword("wrongPassword");

        User user = new User(
                "aryan",
                "aryan@gmail.com",
                "encodedPassword"
        );

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(userRepository).findByUsername("aryan");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }
    @Test
    void login_shouldReturnToken_whenCredentialsAreCorrect() {

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);

        AuthService authService =
                new AuthService(userRepository, passwordEncoder, jwtService);

        LoginRequest request = new LoginRequest();
        request.setUsername("aryan");
        request.setPassword("123456");

        User user = new User(
                "aryan",
                "aryan@gmail.com",
                "encodedPassword"
        );

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken("aryan"))
                .thenReturn("jwt-token");

        LoginResponse result = authService.login(request);

        assertEquals("aryan", result.getUsername());
        assertEquals("jwt-token", result.getToken());

        verify(userRepository).findByUsername("aryan");
        verify(passwordEncoder).matches("123456", "encodedPassword");
        verify(jwtService).generateToken("aryan");
    }
}