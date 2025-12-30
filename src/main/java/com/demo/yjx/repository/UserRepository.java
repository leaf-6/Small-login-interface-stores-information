package com.demo.yjx.repository;

import com.demo.yjx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问接口
 *
 * 继承 JpaRepository<实体类型, 主键类型>
 * 自动提供了常用的 CRUD 方法
 */
@Repository  // 标记为 Spring 管理的 Bean
public interface UserRepository extends JpaRepository<User, Long> {

    // 不需要实现，Spring Data JPA 会自动生成实现类

    /**
     * 根据用户名查询用户
     *
     * 方法命名规则：findBy + 属性名（首字母大写）
     * Spring 会自动生成 SQL：
     * SELECT * FROM users WHERE username = ?
     *
     * @param username 用户名
     * @return Optional 包装的用户对象
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     *
     * 方法命名规则：existsBy + 属性名
     * 生成 SQL：
     * SELECT COUNT(*) > 0 FROM users WHERE username = ?
     *
     * @param username 用户名
     * @return 存在返回 true，否则返回 false
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 存在返回 true，否则返回 false
     */
    boolean existsByEmail(String email);
}