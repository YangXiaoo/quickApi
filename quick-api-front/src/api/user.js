import request from '@/utils/request'
const BASE_URL = '' // process.env.VUE_APP_BASE_API

export function login(data) {
  return request({
    url: BASE_URL + '/login/loginCheck',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: BASE_URL + '/login/getInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: BASE_URL + '/login/logout',
    method: 'post'
  })
}
