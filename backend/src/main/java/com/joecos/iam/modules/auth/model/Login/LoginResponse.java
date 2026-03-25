package com.joecos.iam.modules.auth.model.Login;

import com.joecos.iam.modules.auth.model.AuthResult;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    AuthResult user;
}
