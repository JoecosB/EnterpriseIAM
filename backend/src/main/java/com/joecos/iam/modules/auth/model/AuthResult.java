package com.joecos.iam.modules.auth.model;

import java.util.List;
import lombok.Data;

@ Data
public class AuthResult {
    private Long userId;
    private String username;
    private List<String> permissions;
}
