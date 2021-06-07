import request from '@/utils/request'

/** 测试项目 - 获得个人参与的测试项目 */
export function getProjectInfoList(data) {
  return request({
    url: '/getProjectInfoList',
    method: 'post',
    data
  })
}

export function getProjectDevelopers(data) {
  return request({
    url: '/getProjectDevelopers',
    method: 'post',
    data
  })
}

export function getRunningService(data) {
  return request({
    url: '/getRunningService',
    method: 'post',
    data
  })
}
