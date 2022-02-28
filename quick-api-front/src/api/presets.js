import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_SERVER_PATH

export function getUserHeaderPresets(data) {
  return request({
    url: baseUrl + '/presets/getUserHeaderPresets',
    method: 'post',
    data
  })
}

export function addUserHeaderPresets(data) {
  return request({
    url: baseUrl + '/presets/addUserHeaderPresets',
    method: 'post',
    data
  })
}

export function deleteUserHeaderPresets(data) {
  return request({
    url: baseUrl + '/presets/deleteUserHeaderPresets',
    method: 'post',
    data
  })
}
