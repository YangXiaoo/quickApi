import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SERVER_PATH

export function login(data) {
  return request({
    url: baseUrl + '/login/loginCheck',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: baseUrl + '/login/getInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: baseUrl + '/login/logout',
    method: 'post',
    data: {}
  })
}
