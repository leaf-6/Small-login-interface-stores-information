package com.demo.yjx.controller;

import com.demo.yjx.dto.ApiResponse;
import com.demo.yjx.dto.AuthResponse;
import com.demo.yjx.dto.LoginRequest;
import com.demo.yjx.dto.RegisterRequest;
import com.demo.yjx.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 提供用户注册、登录接口
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户注册
     *
     * POST /api/auth/register
     * {
     *   "username": "testuser",
     *   "password": "12345678",
     *   "email": "test@example.com"
     * }
     *
     * @param request 注册请求
     * @return 包含 Token 的响应
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", response));
    }

    /**
     * 用户登录
     *
     * POST /api/auth/login
     * {
     *   "username": "testuser",
     *   "password": "12345678"
     * }
     *
     * @param request 登录请求
     * @return 包含 Token 的响应
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
}