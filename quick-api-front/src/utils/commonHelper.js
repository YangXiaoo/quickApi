/**
 * 请求参数转换为表数据对象
 * @param {*} getTypeParam 参数
 */
export function parseParams(getTypeParam) {
  const getTypeParamValues = []
  if (getTypeParam) {
    for (const key in getTypeParam) {
      // 定义表数据格式
      const tableItem = {
        name: null, // 参数名
        value: null, // 参数值
        description: null // 说明
      }

      tableItem.name = key
      tableItem.value = getTypeParam[key]
      tableItem.description = ''

      getTypeParamValues.push(tableItem)
    }
  }

  return getTypeParamValues
}
