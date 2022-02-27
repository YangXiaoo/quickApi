import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_LOCAL_PATH

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
    url: baseUrl + '/callApi',
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
    url: baseUrl + '/getLocalConnection',
    method: 'post',
    data
  })
}

export function getDeleteMethodList(data = {}) {
  return request({
    url: baseUrl + '/getDeleteMethodList',
    method: 'post',
    data
  })
}

export function getUploadMethodList(data = {}) {
  return request({
    url: baseUrl + '/getUploadMethodList',
    method: 'post',
    data
  })
}

export function syncProjectApiMethod(data) {
  return request({
    url: baseUrl + '/syncProjectApiMethod',
    method: 'post',
    data
  })
}

export function requestLocalBlobApi(url, data) {
  return request({
    url: url,
    method: 'post',
    data,
    type: 'Blob'
  })
}

export function responseLocalBlobApi(url, data) {
  return request({
    url: url,
    method: 'post',
    data,
    responseType: 'Blob'
  })
}
