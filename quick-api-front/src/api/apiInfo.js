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
    url: '/api/service/saveMethodApiData',
    method: 'post',
    data
  })
}

export function getPageData(path) {
  return request({
    url: '/api/service/getMethodApiData',
    method: 'post',
    data: {
      path: path
    }
  })
}

export function getProjectData(projectName) {
  console.log('getProjectData')
  return request({
    url: 'http://localhost:8899/api/service/getMethodDataByProjectName',
    method: 'post',
    data: {
      projectName: projectName
    }
  })
}