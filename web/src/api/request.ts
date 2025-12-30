import axios from 'axios'
import type { ApiResponse } from '@/types'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从localStorage获取token并添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res: ApiResponse = response.data
    
    // 如果返回的状态码为200，直接返回data
    if (res.code === 200) {
      return res.data
    } else {
      // 显示错误消息
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error) => {
    // 获取后端返回的错误消息
    const backendMessage = error.response?.data?.message
    
    // 处理401未授权错误
    if (error.response && error.response.status === 401) {
      // 如果是登录或注册接口的401，直接显示后端返回的错误消息
      const isAuthApi = error.config?.url?.includes('/auth/')
      
      if (isAuthApi) {
        // 登录/注册失败，显示后端返回的具体错误信息
        ElMessage.error(backendMessage || '认证失败')
      } else {
        // 其他接口的401，说明token过期或未登录
        ElMessage.error(backendMessage || '登录已过期，请重新登录')
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('userId')
        router.push('/login')
      }
    } else {
      // 其他错误，优先使用后端返回的消息
      ElMessage.error(backendMessage || error.message || '请求失败')
    }
    return Promise.reject(error)
  }
)

export default request
