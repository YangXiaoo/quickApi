import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_LOCAL_PATH
const SERVER_PATH = process.env.VUE_APP_SERVER_PATH
const isLocalProject = process.env.VUW_APP_IS_LOCAL

/** 测试项目 - 本地 - 转发本地项目接口 */
export function callApi(path, contentType, headerJson, queryData, type) {
  const params = {
    path: path,
    contentType: contentType,
    headerJson: headerJson,
    queryData: queryData,
    type: type
  }

  return request({
    url: (isLocalProject ? baseUrl : SERVER_PATH) + '/callApi',
    method: 'get',
    params
  })
}

/** 测试项目 - 本地 - 获得接口信息 */
export function getLocalProjectData(params) {
  return request({
    url: baseUrl + '/api',
    method: 'get',
    params
  })
}

export function getConnection(data) {
  return request({
    url: (isLocalProject ? baseUrl : SERVER_PATH) + '/getLocalConnection',
    method: 'post',
    data
  })
}
