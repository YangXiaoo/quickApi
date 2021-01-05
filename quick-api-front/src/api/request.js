import request from '@/utils/request'

/**
 * 请求参数
 * @param {*} path 
 * @param {*} contentType 
 * @param {*} headerJson 
 * @param {*} queryData 
 * @param {*} type 
 */
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
