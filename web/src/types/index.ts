/**
 * 待办事项状态枚举
 */
export const TodoStatus = {
  PENDING: 'PENDING',
  IN_PROGRESS: 'IN_PROGRESS',
  COMPLETED: 'COMPLETED',
  CANCELLED: 'CANCELLED'
} as const

export type TodoStatus = typeof TodoStatus[keyof typeof TodoStatus]

/**
 * 待办事项状态配置
 */
export const TODO_STATUS_CONFIG = {
  [TodoStatus.PENDING]: {
    label: '待办',
    color: '#E6A23C',
    type: 'warning' as const
  },
  [TodoStatus.IN_PROGRESS]: {
    label: '进行中',
    color: '#409EFF',
    type: 'primary' as const
  },
  [TodoStatus.COMPLETED]: {
    label: '已完成',
    color: '#67C23A',
    type: 'success' as const
  },
  [TodoStatus.CANCELLED]: {
    label: '已取消',
    color: '#909399',
    type: 'info' as const
  }
}

/**
 * 用户接口
 */
export interface User {
  id: number
  username: string
  email: string
}

/**
 * 待办事项接口
 */
export interface TodoItem {
  id: number
  title: string
  description: string | null
  status: TodoStatus
  createdAt: string
  updatedAt: string
}

/**
 * 通用API响应接口
 */
export interface ApiResponse<T = any> {
  code: number,
  message: string
  data: T
}

/**
 * 分页响应接口
 */
export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
}

/**
 * 登录请求接口
 */
export interface LoginRequest {
  username: string
  password: string
}

/**
 * 注册请求接口
 */
export interface RegisterRequest {
  username: string
  password: string
  email: string
}

/**
 * 待办事项请求接口
 */
export interface TodoRequest {
  title: string
  description?: string
  status?: TodoStatus
}

/**
 * 认证响应接口
 */
export interface AuthResponse {
  token: string
  username: string
  userId: number
}
