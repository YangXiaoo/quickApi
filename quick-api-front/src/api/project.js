import request from '@/utils/request'

/** 测试项目 - 获得个人参与的测试项目 */
export function getProjectInfoList(data) {
  return request({
    url: '/api/quickApi/getProjectInfoList',
    method: 'post',
    data
  })
}