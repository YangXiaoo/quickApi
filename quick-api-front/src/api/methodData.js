import request from '@/utils/request'

/** 个人接口 - 获得用户的接口方法 */
export function getUserMethodDataList(data) {
  return request({
    url: '/api/quickApi/getUserMethodDataList',
    method: 'post',
    data
  })
}

/** 个人接口 - 保存用户接口方法 */
export function saveUserMethodData(data) {
  return request({
    url: '/api/quickApi/saveUserMethodData',
    method: 'post',
    data
  })
}

/** 修改用户方法信息 */
export function updateUserMethodData(data) {
  return request({
    url: '/api/quickApi/updateUserMethodData',
    method: 'post',
    data
  })
}

/** 测试项目 - 根据项目名获得所有方法数据 */
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

/** 测试项目 - 更新方法信息 */
export function updateMethodData(data) {
  return request({
    url: '/api/quickApi/updateMethodData',
    method: 'post',
    data
  })
}
