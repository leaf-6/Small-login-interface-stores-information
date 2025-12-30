import request from './request'
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/types'

/**
 * 用户登录
 */
export function login(data: LoginRequest): Promise<AuthResponse> {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户注册
 */
export function register(data: RegisterRequest): Promise<AuthResponse> {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}
