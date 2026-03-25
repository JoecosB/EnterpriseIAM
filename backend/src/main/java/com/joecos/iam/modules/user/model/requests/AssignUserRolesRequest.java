package com.joecos.iam.modules.user.model.requests;

import lombok.Data;

import java.util.List;

@Data
public class AssignUserRolesRequest {
    List<String> roleNames;
}
