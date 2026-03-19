package com.joecos.iam.security.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserPrincipal {
    private final Long userId;
    private final String username;
}
