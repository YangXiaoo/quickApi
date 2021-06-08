import request from '@/utils/request'

/**
 * 获取用户页面数据
 */
export function getUserProjectMethodPageData(data) {
  return request({
    url: '/getUserProjectMethodPageData',
    method: 'post',
    data
  })
}

/**
 * 保存用户页面数据
 */
export function saveUserProjectMethodPageData(data) {
  return request({
    url: '/saveUserProjectMethodPageData',
    method: 'post',
    data
  })
}