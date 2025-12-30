package com.demo.yjx.controller;

import com.demo.yjx.dto.ApiResponse;
import com.demo.yjx.dto.PageResponse;
import com.demo.yjx.dto.TodoRequest;
import com.demo.yjx.dto.TodoResponse;
import com.demo.yjx.enums.TodoStatus;
import com.demo.yjx.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * 创建待办事项
     * POST /api/todos
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(
            @Valid @RequestBody TodoRequest request) {

        Long userId = getCurrentUserId();
        TodoResponse response = todoService.createTodo(request, userId);
        return ResponseEntity.ok(ApiResponse.success("创建成功", response));
    }

    /**
     * 分页查询待办事项
     * GET /api/todos?page=0&size=10&status=PENDING
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TodoResponse>>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TodoStatus status) {

        Long userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<TodoResponse> response = todoService.getTodos(userId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 根据 ID 查询待办事项
     * GET /api/todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        TodoResponse response = todoService.getTodoById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 更新待办事项
     * PUT /api/todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request) {

        Long userId = getCurrentUserId();
        TodoResponse response = todoService.updateTodo(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("更新成功", response));
    }

    /**
     * 删除待办事项
     * DELETE /api/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        todoService.deleteTodo(id, userId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    /**
     * 从 SecurityContext 获取当前登录用户的 ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(authentication.getPrincipal().toString());
    }
}