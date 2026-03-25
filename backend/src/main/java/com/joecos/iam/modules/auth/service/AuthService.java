package com.joecos.iam.modules.auth.service;

import com.joecos.iam.modules.auth.model.AuthResult;

public interface AuthService {
    Boolean loginByUsername(String username, String password);
    AuthResult loadUserById(Long userId);
    AuthResult loadUserByUsername(String Username);
}
