import request from '@/utils/request'

const baseUrl = process.env.VUE_APP_SERVER_PATH

export function getUserPresets(data) {
  return request({
    url: baseUrl + '/presets/getUserPresets',
    method: 'post',
    data
  })
}

export function addUserPresets(data) {
  return request({
    url: baseUrl + '/presets/addUserPresets',
    method: 'post',
    data
  })
}

export function deleteUserPresets(data) {
  return request({
    url: baseUrl + '/presets/deleteUserPresets',
    method: 'post',
    data
  })
}
