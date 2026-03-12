package com.joecos.iam.modules.auth.controller;

import com.joecos.iam.modules.auth.model.AuthResult;
import com.joecos.iam.modules.auth.model.LoginRequest;
import com.joecos.iam.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResult login(@RequestBody LoginRequest loginRequest) {
        boolean loginSuccess = authService.loginByUsername(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (!loginSuccess) {
            throw new RuntimeException("Invalid username or password");
        }

        return authService.loadUserById(
                authService.getUserIdByUsername(loginRequest.getUsername())
        );
    }
}
