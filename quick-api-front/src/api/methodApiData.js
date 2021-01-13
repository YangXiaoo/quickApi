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

/** 获得用户的接口方法 */
export function getUserMethodDataList(data) {
  return request({
    url: '/api/quickApi/getUserMethodDataList',
    method: 'post',
    data
  })
}

/** 保存用户的页面数据 */
export function saveUserPageData(data) {
  return request({
    url: '/api/quickApi/saveUserPageData',
    method: 'post',
    data
  })
}

export function updateUserMethodData(data) {
  return request({
    url: '/api/quickApi/updateUserMethodData',
    method: 'post',
    data
  })
}