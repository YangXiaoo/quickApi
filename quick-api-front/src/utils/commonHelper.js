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
      tableItem.value = JSON.stringify(data[key])
      tableItem.description = '' // 说明

      if (data[key] instanceof Object && Object.keys(data[key]).length) {
        tableItem.children = []
        for (const tmpKey in data[key]) {
          tableItem.children.push({
            name: tmpKey, // 参数名
            value: JSON.stringify(data[key][tmpKey]),
            description: ''
          })
        }
      }

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
        required: 'true',
        description: JSON.stringify(data[key]) // 说明
      }
      if (data[key] instanceof Object && Object.keys(data[key]).length) {
        tableItem.children = []
        for (const tmpKey in data[key]) {
          tableItem.children.push({
            name: tmpKey, // 参数名
            type: typeof data[key][tmpKey],
            required: 'true',
            description: JSON.stringify(data[key][tmpKey]) // 说明

          })
        }
      }

      dataList.push(tableItem)
    }
  }

  return dataList
}

/**
 * 解析Postman文件获得路由
 * @param {Object} data
 */
export function getRoutersPostmanApiJsonData(data) {
}
