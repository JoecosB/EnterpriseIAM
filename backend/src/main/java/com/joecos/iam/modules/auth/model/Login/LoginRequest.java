package com.joecos.iam.modules.auth.model.Login;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
