package com.demo.yjx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应 DTO
 * 用于返回登录/注册后的用户信息和 Token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /**
     * JWT Token
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户 ID
     */
    private Long userId;
}