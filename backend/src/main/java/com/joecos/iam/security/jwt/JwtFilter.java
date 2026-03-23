package com.joecos.iam.security.jwt;

import com.joecos.iam.infrastructure.persistence.entity.UserEntity;
import com.joecos.iam.modules.user.service.UserService;
import com.joecos.iam.security.auth.AuthorityBuilder;
import com.joecos.iam.security.model.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // 读取 Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 提取并验证 Token
        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 加载用户
        Long userId = jwtService.extractUserId(token);
        UserEntity user = userService.findUserById(userId);
        String username = user.getUsername();
        List<String> permissionCodes = userService.findPermissionCodesById(userId);

        // 构造 Authentication
        List<GrantedAuthority> authorities = AuthorityBuilder.build(permissionCodes);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        new UserPrincipal(userId, username),
                        null,
                        authorities
                );

        // 构造 Security Context
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
