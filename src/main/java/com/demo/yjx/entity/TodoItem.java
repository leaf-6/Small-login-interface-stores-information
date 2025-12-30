package com.demo.yjx.entity;

import com.demo.yjx.enums.TodoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 待办事项实体类
 *
 * 对应数据库表：todo_items
 */
@Entity
@Table(name = "todo_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 待办事项标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200")
    @Column(nullable = false)
    private String title;

    /**
     * 待办事项描述
     */
    @Size(max = 1000, message = "描述长度不能超过1000")
    @Column(length = 1000)
    private String description;

    /**
     * 待办事项状态
     *
     * @Enumerated(EnumType.STRING): 存储枚举的名称而非序号
     * 优点：数据库中直观，添加新状态不影响旧数据
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status = TodoStatus.PENDING;  // 默认值

    /**
     * 所属用户
     *
     * @ManyToOne: 多对一关系（多个待办事项属于一个用户）
     * @JoinColumn: 外键列配置
     * fetch = FetchType.LAZY: 懒加载（需要时才查询用户信息）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}