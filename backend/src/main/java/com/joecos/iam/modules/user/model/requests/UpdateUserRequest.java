package com.joecos.iam.modules.user.model.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String username;
    String email;
    String password;
}
