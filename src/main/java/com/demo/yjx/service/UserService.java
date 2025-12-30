package com.demo.yjx.service;

import com.demo.yjx.dto.AuthResponse;
import com.demo.yjx.dto.LoginRequest;
import com.demo.yjx.dto.RegisterRequest;
import com.demo.yjx.entity.User;
import com.demo.yjx.exception.BusinessException;
import com.demo.yjx.repository.UserRepository;
import com.demo.yjx.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务类
 * 处理用户注册、登录等业务逻辑
 */
@Service
@RequiredArgsConstructor  // Lombok 注解：自动生成包含 final 字段的构造函数
public class UserService {

    // 依赖注入（通过构造函数注入，不需要 @Autowired）
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 用户注册
     *
     * @param request 注册请求 DTO
     * @return 认证响应（包含 Token）
     */
    @Transactional  // 事务管理：如果发生异常，自动回滚
    public AuthResponse register(RegisterRequest request) {
        // 1. 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 检查邮箱是否已被使用
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被使用");
        }

        // 3. 创建用户实体
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));  // 加密密码
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        // 4. 保存到数据库
        User savedUser = userRepository.save(user);

        // 5. 生成 JWT Token
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getId());

        // 6. 返回响应
        return new AuthResponse(token, savedUser.getUsername(), savedUser.getId());
    }

    /**
     * 用户登录
     *
     * @param request 登录请求 DTO
     * @return 认证响应（包含 Token）
     */
    public AuthResponse login(LoginRequest request) {
        // 1. 根据用户名查询用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));

        // 2. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 3. 生成 JWT Token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        // 4. 返回响应
        return new AuthResponse(token, user.getUsername(), user.getId());
    }
}