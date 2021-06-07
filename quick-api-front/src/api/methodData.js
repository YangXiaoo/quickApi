import request from '@/utils/request'

/** 个人接口 - 获得用户的接口方法 */
export function getUserMethodDataList(data) {
  return request({
    url: '/getUserMethodDataList',
    method: 'post',
    data
  })
}

/** 个人接口 - 保存用户接口方法 */
export function saveUserMethodData(data) {
  return request({
    url: '/saveUserMethodData',
    method: 'post',
    data
  })
}

export function deleteUserMethodData(data) {
  return request({
    url: '/deleteUserMethodData',
    method: 'post',
    data
  })
}

/** 修改用户方法信息 */
export function updateUserMethodData(data) {
  return request({
    url: '/updateUserMethodData',
    method: 'post',
    data
  })
}

/** 测试项目 - 根据项目名获得所有方法数据 */
export function getMethodDataByProjectName(data) {
  return request({
    url: '/getMethodDataByProjectName',
    method: 'post',
    data
  })
}

/** 测试项目 - 更新方法信息 */
export function updateMethodData(data) {
  return request({
    url: '/updateMethodData',
    method: 'post',
    data
  })
}

export function deleteUserMethodApiData(data) {
  return request({
    url: '/deleteUserMethodApiData',
    method: 'post',
    data
  })
}

export function getProjectFinishedMethodDataCount(data) {
  return request({
    url: '/getProjectFinishedMethodDataCount',
    method: 'post',
    data
  })
}

export function getProjectFinishedMethodDataMap(data) {
  return request({
    url: '/getProjectFinishedMethodDataMap',
    method: 'post',
    data
  })
}

