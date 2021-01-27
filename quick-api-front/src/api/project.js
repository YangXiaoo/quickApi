import request from '@/utils/request'

/** 测试项目 - 获得个人参与的测试项目 */
export function getProjectInfoList(data) {
  return request({
    url: '/api/quickApi/getProjectInfoList',
    method: 'post',
    data
  })
}

export function getProjectDevelopers(data) {
  return request({
    url: '/api/quickApi/getProjectDevelopers',
    method: 'post',
    data
  })
}

export function getRunningService(data) {
  return request({
    url: '/api/quickApi/getRunningService',
    method: 'post',
    data
  })
}
