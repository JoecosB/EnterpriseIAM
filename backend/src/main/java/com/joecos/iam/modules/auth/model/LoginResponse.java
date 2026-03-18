package com.joecos.iam.modules.auth.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String token;
    AuthResult user;
}
