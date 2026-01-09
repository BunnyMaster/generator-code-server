/**
 * 方法1: 使用URLSearchParams API
 * @param search window.location.search
 * @returns {{}}
 */
const parseWithURLSearchParams = (search) => {
    const params = new URLSearchParams(search);
    const result = {};
    for (const [key, value] of params) {
        result[key] = value;
    }
    return result;
}

/**
 * 方法2: 使用传统字符串分割
 * @param search window.location.search
 * @returns {{}}
 */
const parseWithStringSplit = (search) => {
    const result = {};
    const queryString = search.startsWith('?') ? search.substring(1) : search;
    const pairs = queryString.split('&');

    for (const pair of pairs) {
        const [key, value] = pair.split('=');
        if (key) {
            result[decodeURIComponent(key)] = decodeURIComponent(value || '');
        }
    }
    return result;
}