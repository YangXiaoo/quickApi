import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_SERVER_PATH

/** 测试项目 - 本地 - 转发本地项目接口 */
export function callServiceApi(path, contentType, headerJson, queryData, type) {
  const params = {
    path: path,
    contentType: contentType,
    headerJson: headerJson,
    queryData: queryData,
    type: type
  }

  return request({
    url: baseUrl + '/callApi',
    method: 'get',
    params
  })
}

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

export function saveMethodData(data) {
  return request({
    url: baseUrl + '/saveMethodData',
    method: 'post',
    data
  })
}
