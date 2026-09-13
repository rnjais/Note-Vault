package com.Note_Vault.service;

import com.Note_Vault.dto.LoginRequest;
import com.Note_Vault.dto.LoginResponse;
import com.Note_Vault.dto.RegisterRequest;
import com.Note_Vault.entity.User;
import com.Note_Vault.exception.EmailAlreadyExistsException;
import com.Note_Vault.exception.InvalidCredentialsException;
import com.Note_Vault.exception.UsernameAlreadyExistsException;
import com.Note_Vault.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if(userRepository.existsByEmail((request.getEmail()))){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        return userRepository.save(user);
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                user.getUsername(),
                token
        );

    }
}
