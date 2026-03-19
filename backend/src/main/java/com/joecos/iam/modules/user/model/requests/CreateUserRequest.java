package com.joecos.iam.modules.user.model.requests;

import lombok.Data;

@Data
public class CreateUserRequest {
    String username;
    String password;
}
