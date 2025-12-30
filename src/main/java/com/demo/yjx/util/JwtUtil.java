package com.demo.yjx.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 工具类
 * 用于生成、验证和解析 JWT Token
 */
@Component
public class JwtUtil {

    // 从配置文件读取密钥（必须至少 32 字节）
    @Value("${jwt.secret}")
    private String secret;

    // Token 有效期（默认 24 小时，单位：毫秒）
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成签名密钥
     * 将配置的密钥字符串转换为 SecretKey 对象
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT Token
     *
     * @param username 用户名
     * @param userId 用户ID
     * @return JWT Token 字符串
     */
    public String generateToken(String username, Long userId) {
        // 创建自定义声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, username);
    }

    /**
     * 创建 Token（内部方法）
     * 实际执行 Token 生成的核心逻辑
     *
     * @param claims 自定义声明
     * @param subject 主题（通常是用户名）
     * @return JWT Token 字符串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        // 获取当前时间
        Date now = new Date();
        // 计算过期时间
        Date expirationDate = new Date(now.getTime() + expiration);

        // 构建并返回 Token
        return Jwts.builder()
                .claims(claims)                  // 设置自定义声明
                .subject(subject)                // 设置主题
                .issuedAt(now)                   // 设置签发时间
                .expiration(expirationDate)      // 设置过期时间
                .signWith(getSigningKey())       // 使用密钥签名
                .compact();                      // 生成最终的 Token 字符串
    }

    /**
     * 从 Token 中提取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 Token 中提取用户 ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Token 中提取过期时间
     *
     * @param token JWT Token
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 提取指定的声明
     * 使用函数式接口从 Claims 中提取特定字段
     *
     * @param token JWT Token
     * @param claimsResolver 声明解析函数
     * @return 声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 提取所有声明
     * 解析 Token 并返回所有的 Payload 数据
     *
     * @param token JWT Token
     * @return 所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())     // 使用密钥验证签名
                .build()
                .parseSignedClaims(token)        // 解析 Token
                .getPayload();                   // 获取 Payload
    }

    /**
     * 检查 Token 是否过期
     *
     * @param token JWT Token
     * @return true 表示已过期
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 验证 Token 是否有效
     * 检查用户名是否匹配且 Token 未过期
     *
     * @param token JWT Token
     * @param username 用户名
     * @return true 表示有效
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}