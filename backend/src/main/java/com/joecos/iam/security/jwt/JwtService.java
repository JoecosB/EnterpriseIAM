package com.joecos.iam.security.jwt;

import io.jsonwebtoken.Claims;

public interface JwtService {

    /** 生成 JWT Token */
    String generateToken(Long userId, String username);

    /** 从 Token 中解析 claim */
    Claims extractClaim(String token);

    /** 从 claim 中解析 userId */
    Long extractUserId(Claims claim);

    /** 从 claim 中解析 username */
    String extractUsername(Claims claim);

    /** 校验 Token 是否有效 */
    boolean isTokenValid(String token);

}