package com.demo.yjx.dto;

import com.demo.yjx.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 待办事项响应 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {

    /**
     * 待办事项 ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    private TodoStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}