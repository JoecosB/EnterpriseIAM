package com.joecos.iam.modules.user.model;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    Long userId;
    String username;
    List<String> userRole;
}
