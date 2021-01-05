/**
 * 是否以某字符串结尾
 * @param {*} str 
 * @param {*} search 
 */
export function endsWith(str, search) {
    var d = str.length - search.length
    return (d >= 0 && str.lastIndexOf(search) == d)
}
