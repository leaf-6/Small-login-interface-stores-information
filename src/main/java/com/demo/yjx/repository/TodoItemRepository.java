package com.demo.yjx.repository;

import  com.demo.yjx.entity.TodoItem;
import com.demo.yjx.entity.User;
import  com.demo.yjx.enums.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import  org.springframework.stereotype.Repository;
import  java.util.Optional;
@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem,Long> {
    Page<TodoItem> findByUserId(Long userId,Pageable pageable);

    Page<TodoItem> findByUserIdAndStatus(Long userId, TodoStatus status,Pageable pageable);

    Optional<TodoItem>  findByIdAndUserId(Long id ,Long userId);

    Long user(User user);
}
