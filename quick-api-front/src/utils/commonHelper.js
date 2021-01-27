/**
 * 请求参数转换为表数据对象
 * @param {*} data 参数
 */
export function parseParams(data) {
  const dataList = []
  if (data) {
    for (const key in data) {
      // 定义表数据格式
      const tableItem = {
        name: null, // 参数名
        value: null, // 参数值
        description: null // 说明
      }

      tableItem.name = key
      tableItem.value = data[key]
      tableItem.description = ''

      dataList.push(tableItem)
    }
  }

  return dataList
}

/** 根据router.path获得本地项目的URL */
export function getLocalProjectPath(path) {
  if (path) {
    return path.substring(path.substring(1).indexOf('/') + 1)
  }
}

export function parseRequestData(data) {
  const dataList = []
  if (data) {
    for (const key in data) {
      // 定义表数据格式
      const tableItem = {
        name: key, // 参数名
        type: typeof data[key],
        required: 'false',
        description: '' // 说明
      }

      dataList.push(tableItem)
    }
  }

  return dataList
}
