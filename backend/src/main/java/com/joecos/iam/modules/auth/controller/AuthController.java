package com.joecos.iam.modules.auth.controller;

import com.joecos.iam.common.api.ApiResponse;
import com.joecos.iam.modules.auth.model.*;
import com.joecos.iam.modules.auth.model.Login.LoginRequest;
import com.joecos.iam.modules.auth.model.Login.LoginResponse;
import com.joecos.iam.modules.auth.service.AuthService;
import com.joecos.iam.security.jwt.JwtService;
import com.joecos.iam.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
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

        return ApiResponse.success(loginResponse);
    }

    @GetMapping("/me")
    public ApiResponse<AuthResult> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new RuntimeException("Invalid authentication principal");
        }

        return ApiResponse.success(
                authService.loadUserById(userPrincipal.getUserId())
        );
    }
}
