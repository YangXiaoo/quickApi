import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_SERVER_PATH

/**
 * 根据路由获得用户的接口数据
 */
export function getUserMethodApiData(data) {
  return request({
    url: baseUrl + '/getUserMethodApiData',
    method: 'post',
    data
  })
}

/** 保存用户的页面数据 */
export function saveUserMethodApiData(data) {
  return request({
    url: baseUrl + '/saveUserMethodApiData',
    method: 'post',
    data
  })
}

export function saveMethodApiData(data) {
  return request({
    url: baseUrl + '/saveMethodApiData',
    method: 'post',
    data
  })
}

export function getMethodApiData(data) {
  return request({
    url: baseUrl + '/getMethodApiData',
    method: 'post',
    data
  })
}

export function deleteMethodApiData(data) {
  return request({
    url: baseUrl + '/deleteMethodApiData',
    method: 'post',
    data
  })
}

export function getProjectMethodApiDataHistory(data) {
  return request({
    url: baseUrl + '/getProjectMethodApiDataHistory',
    method: 'post',
    data
  })
}
