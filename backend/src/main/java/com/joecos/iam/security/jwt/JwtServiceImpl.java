package com.joecos.iam.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${security.jwt.expiration}")
    Integer expiration;

    @Value("${security.jwt.secretKey}")
    Key secretKey;

    /**
     * 生成用户授权 Token
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
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析用户 Token，提取 userId
     */
    @Override
    public Long extractUserId(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("userId", Long.class);

        } catch (JwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);

        }
    }

    /**
     * 解析用户 Token，提取 username
     */
    @Override
    public String extractUsername(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("username", String.class);

        } catch (JwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);

        }
    }

    /** 验证 Token 是否合法 */
    @Override
    public boolean isTokenValid(String token) {

        try {
            Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException ex) {
            return false;

        }

    }
}