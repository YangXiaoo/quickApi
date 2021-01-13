import request from '@/utils/request'

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
    url: '/api/quickApi/callApi',
    method: 'get',
    params
  })
}

/** 测试项目 - 本地 - 获得接口信息 */
export function getQApiRoutesInfo(params) {
  return request({
    url: '/api/quickApi/api',
    method: 'get',
    params
  })
}
