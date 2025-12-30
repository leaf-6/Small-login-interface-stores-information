package com.demo.yjx.security;

import com.demo.yjx.entity.User;
import com.demo.yjx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 自定义用户详情服务
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 尝试将 username 解析为 userId
        try {
            Long userId = Long.parseLong(username);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

            return new org.springframework.security.core.userdetails.User(
                    String.valueOf(user.getId()), // 使用用户 ID 作为 username
                    user.getPassword(),
                    new ArrayList<>()
            );
        } catch (NumberFormatException e) {
            // 如果不是数字，按用户名查找
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

            return new org.springframework.security.core.userdetails.User(
                    String.valueOf(user.getId()),
                    user.getPassword(),
                    new ArrayList<>()
            );
        }
    }
}