package com.demo.yjx.service;

import com.demo.yjx.dto.PageResponse;
import com.demo.yjx.dto.TodoRequest;
import com.demo.yjx.dto.TodoResponse;
import com.demo.yjx.entity.TodoItem;
import com.demo.yjx.entity.User;
import com.demo.yjx.enums.TodoStatus;
import com.demo.yjx.exception.ResourceNotFoundException;
import com.demo.yjx.repository.TodoItemRepository;
import com.demo.yjx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoItemRepository todoItemRepository;
    private final UserRepository userRepository;

    /**
     * 创建待办事项
     */
    @Transactional
    public TodoResponse createTodo(TodoRequest request, Long userId) {
        // 1. 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        // 2. 创建 TodoItem
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        todoItem.setStatus(request.getStatus() != null ? request.getStatus() : TodoStatus.PENDING);
        todoItem.setUser(user);
        todoItem.setCreatedAt(LocalDateTime.now());
        todoItem.setUpdatedAt(LocalDateTime.now());

        // 3. 保存
        TodoItem saved = todoItemRepository.save(todoItem);

        // 4. 转换为 DTO
        return convertToResponse(saved);
    }

    /**
     * 分页查询待办事项（支持状态过滤）
     */
    public PageResponse<TodoResponse> getTodos(Long userId, TodoStatus status, Pageable pageable) {
        Page<TodoItem> page;

        if (status != null) {
            // 按状态过滤
            page = todoItemRepository.findByUserIdAndStatus(userId, status, pageable);
        } else {
            // 查询所有
            page = todoItemRepository.findByUserId(userId, pageable);
        }

        // 转换为 DTO 列表
        List<TodoResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 构建分页响应
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    /**
     * 根据 ID 查询待办事项
     */
    public TodoResponse getTodoById(Long id, Long userId) {
        TodoItem todoItem = todoItemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("待办事项不存在或无权访问"));

        return convertToResponse(todoItem);
    }

    /**
     * 更新待办事项
     */
    @Transactional
    public TodoResponse updateTodo(Long id, TodoRequest request, Long userId) {
        // 1. 查询并验证权限
        TodoItem todoItem = todoItemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("待办事项不存在或无权访问"));

        // 2. 更新字段
        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            todoItem.setStatus(request.getStatus());
        }
        todoItem.setUpdatedAt(LocalDateTime.now());

        // 3. 保存
        TodoItem updated = todoItemRepository.save(todoItem);

        // 4. 返回 DTO
        return convertToResponse(updated);
    }

    /**
     * 删除待办事项
     */
    @Transactional
    public void deleteTodo(Long id, Long userId) {
        TodoItem todoItem = todoItemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("待办事项不存在或无权访问"));

        todoItemRepository.delete(todoItem);
    }

    /**
     * Entity 转 DTO
     */
    private TodoResponse convertToResponse(TodoItem todoItem) {
        return new TodoResponse(
                todoItem.getId(),
                todoItem.getTitle(),
                todoItem.getDescription(),
                todoItem.getStatus(),
                todoItem.getCreatedAt(),
                todoItem.getUpdatedAt()
        );
    }
}