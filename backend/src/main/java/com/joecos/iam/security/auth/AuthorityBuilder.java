package com.joecos.iam.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorityBuilder {

    /**
     * 构建授权
     *
     * @param permissionCodes 权限代码字符串列表
     * */
    public List<GrantedAuthority> buildGrantedAuthority(List<String> permissionCodes) {
        return permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
