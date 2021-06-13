import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_SERVER_PATH

/**
 * 获取用户页面数据
 */
export function getUserProjectMethodPageData(data) {
  return request({
    url: baseUrl + '/getUserProjectMethodPageData',
    method: 'post',
    data
  })
}

/**
 * 保存用户页面数据
 */
export function saveUserProjectMethodPageData(data) {
  return request({
    url: baseUrl + '/saveUserProjectMethodPageData',
    method: 'post',
    data
  })
}
