import request from '@/utils/request'

/**
 * 根据路由获得用户的接口数据
 */
export function getUserMethodApiData(data) {
  return request({
    url: '/api/quickApi/getUserMethodApiData',
    method: 'post',
    data
  })
}

/** 保存用户的页面数据 */
export function saveUserMethodApiData(data) {
  return request({
    url: '/api/quickApi/saveUserMethodApiData',
    method: 'post',
    data
  })
}

export function saveMethodApiData(data) {
  return request({
    url: '/api/quickApi/saveMethodApiData',
    method: 'post',
    data
  })
}

export function getMethodApiData(data) {
  return request({
    url: '/api/quickApi/getMethodApiData',
    method: 'post',
    data
  })
}

export function deleteMethodApiData(data) {
  return request({
    url: '/api/quickApi/deleteMethodApiData',
    method: 'post',
    data
  })
}

export function getProjectMethodApiDataHistory(data) {
  return request({
    url: '/api/quickApi/getProjectMethodApiDataHistory',
    method: 'post',
    data
  })
}
