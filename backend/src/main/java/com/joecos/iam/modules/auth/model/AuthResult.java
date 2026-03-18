package com.joecos.iam.modules.auth.model;

import lombok.Data;

import java.util.List;

@ Data
public class AuthResult {
    private Long userId;
    private String username;
    private List<String> permissions;
}
