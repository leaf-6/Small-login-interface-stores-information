package com.demo.yjx;

import com.demo.yjx.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateAndValidateToken() {
        // 1. 生成 Token
        String token = jwtUtil.generateToken("testuser", 1L);
        System.out.println("Generated Token: " + token);

        // 2. 提取用户名
        String username = jwtUtil.extractUsername(token);
        System.out.println("Username: " + username);

        // 3. 提取用户 ID
        Long userId = jwtUtil.extractUserId(token);
        System.out.println("User ID: " + userId);

        // 4. 验证 Token
        boolean isValid = jwtUtil.validateToken(token, "testuser");
        System.out.println("Is Valid: " + isValid);

        // 断言
        assertEquals("testuser", username);
        assertEquals(1L, userId);
        assertTrue(isValid);
    }
}