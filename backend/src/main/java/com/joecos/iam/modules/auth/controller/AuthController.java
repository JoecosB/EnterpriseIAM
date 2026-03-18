package com.joecos.iam.modules.auth.controller;

import com.joecos.iam.modules.auth.model.*;
import com.joecos.iam.modules.auth.service.AuthService;
import com.joecos.iam.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        boolean loginSuccess = authService.loginByUsername(username, password);
        if (!loginSuccess) {
            throw new RuntimeException("Invalid username or password");
        }

        AuthResult user = authService.loadUserByUsername(username);
        String token = jwtService.generateToken(user.getUserId(), user.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUser(user);

        return loginResponse;
    }
}
