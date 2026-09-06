package com.Note_Vault.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final String secretKey = "NoteVaultSecretKeyForJwtAuthentication2026VerySecure";

    private final long expirationTime = 1000*60*60;

    private SecretKey  getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    }
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
//        Postman
//            │
//            │ username + password
//           ↓
//        AuthController
//           ↓
//        AuthService
//            ↓
//        Check BCrypt password
//            ↓
//        Correct ✅
//             ↓
//        JwtService
//              ↓
//        generateToken("aryan1")
//              ↓
//        JWT Token
//              ↓
//        Postman
    }
}
//                            NoteVault
//                               │
//                      ┌────────┴────────┐
//                      ↓                 ↓
//                    Register          Login
//                      │                 │
//                      ↓                 ↓
//                   MySQL            Check Password
//                                        │
//                                        ↓
//                                    Generate JWT
//                                        │
//                                        ↓
//                                      Token
//                                        │
//                                        ↓
//                                  User/Postman
//                                        │
//                            ┌───────────┴───────────┐
//                            ↓                       ↓
//                       Notes Request           Notes Request
//                            │                       │
//                            └───────────┬───────────┘
//                                        ↓
//                                   JWT Filter
//                                        ↓
//                                 Validate Token
//                                        ↓
//                               Allow / Reject