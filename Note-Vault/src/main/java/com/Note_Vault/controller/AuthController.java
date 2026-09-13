package com.Note_Vault.controller;

import com.Note_Vault.dto.LoginRequest;
import com.Note_Vault.dto.LoginResponse;
import com.Note_Vault.dto.RegisterRequest;
import com.Note_Vault.dto.UserDTO;
import com.Note_Vault.entity.User;
import com.Note_Vault.mapper.UserMapper;
import com.Note_Vault.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;
    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid  @RequestBody RegisterRequest request){
        User user= authService.register(request);
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
