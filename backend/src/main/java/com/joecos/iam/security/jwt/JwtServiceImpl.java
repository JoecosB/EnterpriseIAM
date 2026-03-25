package com.joecos.iam.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${security.jwt.expiration}")
    Integer expiration;

    @Value("${security.jwt.secretKey}")
    String rawKey;

    private SecretKey getSignKey() {
        if (rawKey != null) {
            return Keys.hmacShaKeyFor(rawKey.getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    /**
     * 生成用户授权 Token
     *
     * @param userId    用户 ID
     * @param username  用户名
     */
    @Override
    public String generateToken(Long userId, String username) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }


    /**
     * 从 Token 中解析 claim
     *
     * @param token 用户授权 token
     */
    @Override
    public Claims extractClaim(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (JwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);

        }
    }


    /**
     * 从 token 中解析 userId
     *
     * @param token 用户 token
     */
    @Override
    public Long extractUserId(String token) {
        Claims claim = extractClaim(token);
        return claim.get("userId", Long.class);
    }


    /**
     * 从 token 中解析 username
     *
     * @param token 从用户 token 中解析的 claim
     */
    @Override
    public String extractUsername(String token) {
        Claims claim = extractClaim(token);
        return claim.get("username", String.class);
    }


    /**
     * 验证 Token 是否合法
     *
     * @param token 用户授权 token
     */
    @Override
    public boolean isTokenValid(String token) {

        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException ex) {
            return false;

        }

    }
}