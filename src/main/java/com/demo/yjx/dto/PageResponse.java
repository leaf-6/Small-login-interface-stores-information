package com.demo.yjx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应 DTO
 * 支持泛型，可以包装任何类型的数据
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 当前页的数据列表
     */
    private List<T> content;

    /**
     * 当前页码（从 0 开始）
     */
    private int currentPage;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 总元素数量
     */
    private long totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 是否是第一页
     */
    private boolean first;

    /**
     * 是否是最后一页
     */
    private boolean last;
}