import request from '@/utils/request'

/**
 * 根据路由获得用户的接口数据
 */
export function getUserMethodApiData(data) {
  return request({
    url: '/getUserMethodApiData',
    method: 'post',
    data
  })
}

/** 保存用户的页面数据 */
export function saveUserMethodApiData(data) {
  return request({
    url: '/saveUserMethodApiData',
    method: 'post',
    data
  })
}

export function saveMethodApiData(data) {
  return request({
    url: '/saveMethodApiData',
    method: 'post',
    data
  })
}

export function getMethodApiData(data) {
  return request({
    url: '/getMethodApiData',
    method: 'post',
    data
  })
}

export function deleteMethodApiData(data) {
  return request({
    url: '/deleteMethodApiData',
    method: 'post',
    data
  })
}

export function getProjectMethodApiDataHistory(data) {
  return request({
    url: '/getProjectMethodApiDataHistory',
    method: 'post',
    data
  })
}
