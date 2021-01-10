import request from '@/utils/request'

export function getQApiRoutesInfo(params) {
  return request({
    url: '/api/quickApi/api',
    method: 'get',
    params
  })
}

export function savePageData(data) {
  return request({
    url: '/api/quickApi/saveMethodApiData',
    method: 'post',
    data
  })
}

export function getMethodApiData(data) {
  return request({
    url: '/api/quickApi/getMethodApiData',
    method: 'post',
    data
  })
}

export function getMethodDataByProjectName(projectName) {
  console.log('getProjectData')
  return request({
    url: '/api/quickApi/getMethodDataByProjectName',
    method: 'post',
    data: {
      projectName: projectName
    }
  })
}

/**
 * 更新方法信息
 * @param {Object} data 方法数据
 */
export function updateMethodData(data) {
  return request({
    url: '/api/quickApi/updateMethodData',
    method: 'post',
    data
  })
}