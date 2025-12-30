package com.demo.yjx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * 对应数据库表：users
 */
@Entity                    // 标记为JPA实体
@Table(name = "users")     // 指定表名
@Data                      // Lombok：自动生成getter/setter/toString等
@NoArgsConstructor         // Lombok：生成无参构造函数
@AllArgsConstructor        // Lombok：生成全参构造函数
public class User {

    /**
     * 主键ID
     *
     * @Id: 标记为主键
     * @GeneratedValue: 自动生成策略
     * IDENTITY: 使用数据库自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     *
     * @NotBlank: 不能为空（包括空白字符）
     * @Size: 长度限制
     * @Column: 列配置
     *   - unique: 唯一约束
     *   - nullable: 非空约束
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 密码（加密后存储）
     *
     * 注意：实体类中不对密码长度做验证
     * 因为存储的是加密后的密码（通常60+字符）
     */
    @NotBlank(message = "密码不能为空")
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱
     *
     * @Email: 验证邮箱格式
     */
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * 创建时间
     *
     * @CreationTimestamp: 自动设置创建时间
     * updatable = false: 创建后不可修改
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}