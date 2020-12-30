import request from '@/utils/request'

const baseURL = 'http://localhost:8899'

export function getQApiRoutesInfo(params) {
  return request({
    url: baseURL + '/quickapi/api',
    method: 'get',
    params
  })
}
