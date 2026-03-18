package com.joecos.iam.security.jwt;

import io.jsonwebtoken.Claims;

public interface JwtService {

    /**
     * 生成用户授权 Token
     *
     * @param userId    用户 ID
     * @param username  用户名
     */
    String generateToken(Long userId, String username);

    /**
     * 从 Token 中解析 claim
     *
     * @param token 用户授权 token
     */
    Claims extractClaim(String token);

    /**
     * 从 token 中解析 userId
     *
     * @param token 用户 token
     */
    Long extractUserId(String token);

    /**
     * 从 token 中解析 username
     *
     * @param token 从用户 token 中解析的 claim
     */
    String extractUsername(String token);

    /**
     * 验证 Token 是否合法
     *
     * @param token 用户授权 token
     */
    boolean isTokenValid(String token);

}