export function setData(key, data) {
  // const dataJson = JSON.stringify(data)
  localStorage.setItem(key, data)
}

export function getData(key) {
  return localStorage.getItem(key)
}

export function deleteData(key) {
  localStorage.removeItem(key)
}
