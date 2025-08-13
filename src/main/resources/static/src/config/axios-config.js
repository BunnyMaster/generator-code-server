// axios 配置
const axiosInstance = axios.create({
    baseURL: 'http://localhost:10068/api',
    timeout: 16000,
    headers: {'Content-Type': 'application/json;charset=utf-8'},
});

// 响应拦截器
axiosInstance.interceptors.response.use(
    (response) => {
        // 检查配置的响应类型是否为二进制类型（'blob' 或 'arraybuffer'）, 如果是，直接返回响应对象
        if (response.config.responseType === 'blob' || response.config.responseType === 'arraybuffer') {
            return response;
        }

        if (response.status === 200) {
            const {code, message} = response.data;
            if (code !== 200) {
                antd.message.error(message);
            }
            return response.data;
        }

        // 系统出错
        return Promise.reject(response.data.message || 'Error');
    },
    (error) => {
        // 异常处理
        if (error.response.data) {
            const {code, message} = error.response.data;
            if (code === 500) {
                antd.message.error(message);
            } else {
                antd.message.error(message || '系统出错');
            }

            return error.response.data;
        }
        return Promise.reject(error.message);
    }
);