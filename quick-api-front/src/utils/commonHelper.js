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

export function parseFormDataRequestData(data) {
  const dataList = []
  if (data) {
    for (let i = 0; i < data.length; ++i) {
      const tmpData = data[i]
      if (!tmpData.key && !tmpData.value) {
        continue
      }
      // 定义表数据格式
      const tableItem = {
        name: tmpData.key, // 参数名
        type: tmpData.type === 'Text' ? 'String' : 'File',
        required: 'true',
        description: tmpData.type === 'Text' ? tmpData.value : 'File' // 说明
      }

      dataList.push(tableItem)
    }
  }

  return dataList
}

/**
 * 解析Postman文件获得路由
 * @param {Object} data
 * @return [{api: {}, pageData: {}}, ...]
 */
export function parsePostmanJsonToQuickApi(userName, data) {
  const apiList = []
  if (!data) {
    return apiList
  }
  if (data.info) {
    const methodGroup = data.info.name
    if (data.item) {
      data.item.forEach(item => {
        const groupPath = S4()
        const uuid = getUUID32()
        const url = '/' + groupPath + '/' + uuid
        const api = {
          methodGroup: methodGroup,
          methodName: item.name,
          requestType: item.request.method,
          url: url,
          userName
        }

        const headerJson = {}
        if (item.request.header) {
          item.request.header.forEach(header => {
            headerJson[header.key] = header.value
          })
        }
        const responseHeader = {}
        if (item.response.header) {
          item.response.header.forEach(header => {
            responseHeader[header.key] = header.value
          })
        }
        const formData = []
        if (item.request.body && item.request.body.formdata) {
          item.request.body.formdata.forEach(data => {
            formData.push({
              key: data.key,
              value: data.value || '',
              type: data.type,
              fileList: []
            })
          })
        }
        let contentType = ''
        let requestActiveName = 'Body'
        const getMethodQuery = {}
        let bodyJsonData = {}
        let bodyNoneShow = false
        let bodyJsonShow = false
        let bodyFormDataShow = false
        if (item.request.method === 'GET') { // get
          contentType = 'none'
          requestActiveName = 'Param'
          if (item.request.url.query) {
            item.request.url.query.forEach(query => {
              getMethodQuery[query.key] = query.value
            })
          }
        } else {
          if (item.request.body && item.request.body.formdata) {
            contentType = 'form-data'
            bodyFormDataShow = true
          } else if (item.request.url.raw === 'post-none') {
            contentType = 'none'
            bodyNoneShow = true
          } else if (item.request.body && item.request.body.raw) {
            contentType = 'application/json'
            bodyJsonShow = true
            bodyJsonData = JSON.parse(item.request.body.raw)
          }
        }
        const apiData = {
          requestActiveName: requestActiveName,
          responseActiveName: 'Body',
          path: item.request.url.raw,
          requestType: item.request.method,
          headerJson: headerJson,
          getTypeParam: getMethodQuery,
          contentType: contentType,
          bodyNoneShow: bodyNoneShow,
          bodyJsonShow: bodyJsonShow,
          bodyFormDataShow: bodyFormDataShow,
          bodyJsonData: bodyJsonData,
          bodyStringData: '',
          requestServiceName: '',
          formData: formData,
          responseHeader: responseHeader,
          responseBody: item.response.body
        }
        const pageData = {
          apiData: JSON.stringify(apiData),
          url: url,
          userName
        }

        apiList.push({
          api,
          pageData
        })
      })
    }

    return apiList
  }
  // methodGroup: "ccc"
  // methodName: "ddd"
  // requestType: "POST"
  // url: "/dace/c796b02a-e4ba-0262-f67d-576d3fe16fbd"
  // userName: "test"

  // apiData: "{"requestActiveName":"Body","responseActiveName":"Body","path":"ddd","requestType":"POST","headerJson":null,"getTypeParam":null,"contentType":"none","bodyNoneShow":true,"bodyJsonShow":false,"bodyFileShow":false,"bodyJsonData":null,"bodyStringData":null,"requestServiceName":"","formData":[{"key":"","value":"","type":"Text","fileList":[]}],"responseHeader":null,"responseBody":null}"
  // url: "/dace/c796b02a-e4ba-0262-f67d-576d3fe16fbd"
  // userName: "test"
}

export function getUUID32() {
  return (S4() + S4() + '-' + S4() + '-' + S4() + '-' + S4() + '-' + S4() + S4() + S4())
}

function S4() {
  return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
}
