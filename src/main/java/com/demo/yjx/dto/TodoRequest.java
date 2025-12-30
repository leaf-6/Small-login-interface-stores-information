package com.demo.yjx.dto;

import com.demo.yjx.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 待办事项请求 DTO
 */
@Data
public class TodoRequest {

    /**
     * 标题
     * - 不能为空
     * - 最大长度 200
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过 200")
    private String title;

    /**
     * 描述
     * - 可以为空
     * - 最大长度 1000
     */
    @Size(max = 1000, message = "描述长度不能超过 1000")
    private String description;

    /**
     * 状态
     * - 可以为 null（创建时默认为 PENDING）
     * - 更新时可以修改
     */
    private TodoStatus status;
}