import request from './request'
import type { TodoItem, TodoRequest, PageResponse, TodoStatus } from '@/types'

/**
 * 获取待办事项列表（分页）
 */
export function getTodos(params: {
  page: number
  size: number
  status?: TodoStatus | ''
}): Promise<PageResponse<TodoItem>> {
  return request({
    url: '/todos',
    method: 'get',
    params: {
      page: params.page,
      size: params.size,
      ...(params.status ? { status: params.status } : {})
    }
  })
}

/**
 * 获取单个待办事项
 */
export function getTodoById(id: number): Promise<TodoItem> {
  return request({
    url: `/todos/${id}`,
    method: 'get'
  })
}

/**
 * 创建待办事项
 */
export function createTodo(data: TodoRequest): Promise<TodoItem> {
  return request({
    url: '/todos',
    method: 'post',
    data
  })
}

/**
 * 更新待办事项
 */
export function updateTodo(id: number, data: TodoRequest): Promise<TodoItem> {
  return request({
    url: `/todos/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除待办事项
 */
export function deleteTodo(id: number): Promise<void> {
  return request({
    url: `/todos/${id}`,
    method: 'delete'
  })
}
