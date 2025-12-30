import dayjs from 'dayjs'

/**
 * 格式化日期时间
 * @param date 日期字符串或Date对象
 * @returns 格式化后的日期时间字符串 YYYY-MM-DD HH:mm
 */
export function formatDate(date: string | Date): string {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}
