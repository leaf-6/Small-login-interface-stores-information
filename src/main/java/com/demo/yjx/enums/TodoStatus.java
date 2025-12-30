package com.demo.yjx.enums;

/**
 * 待办事项状态枚举
 *
 * 枚举的优势：
 * 1. 类型安全：编译时检查，避免无效值
 * 2. 代码清晰：见名知义
 * 3. 易于维护：统一管理所有状态
 */
public enum TodoStatus {
        /**
         * 待办 - 刚创建的待办事项
         */
        PENDING,

        /**
         * 进行中 - 正在处理的待办事项
         */
        IN_PROGRESS,

        /**
         * 已完成 - 已经完成的待办事项
         */
        COMPLETED,

        /**
         * 已取消 - 不再需要的待办事项
         */
        CANCELLED
}