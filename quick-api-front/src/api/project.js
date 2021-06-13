import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_SERVER_PATH

/** 测试项目 - 获得个人参与的测试项目 */
export function getProjectInfoList(data) {
  return request({
    url: baseUrl + '/getProjectInfoList',
    method: 'post',
    data
  })
}

export function getProjectDevelopers(data) {
  return request({
    url: baseUrl + '/getProjectDevelopers',
    method: 'post',
    data
  })
}

export function getRunningService(data) {
  return request({
    url: baseUrl + '/getRunningService',
    method: 'post',
    data
  })
}
