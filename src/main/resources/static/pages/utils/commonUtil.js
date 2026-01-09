/**
 * 显示消息提示框
 */
const message = ({message, type}) => {
    // 设置默认配置
    message = message || "";
    type = type || "info";

    // 调用ElementPlus的消息组件显示消息
    ElementPlus.ElMessage({message, type, plain: true, showClose: true,});
};

/**
 * 打开消息确认框函数
 */
const openMessageBox = async ({text, title, type}) => {
    // 设置默认值
    text = text || "确定要删除吗？";
    title = title || "提示";
    type = type || "warning";

    try {
        // 等待用户确认操作
        await ElementPlus.ElMessageBox.confirm(text, title, {
            confirmButtonText: '确认',
            cancelButtonText: '返回',
            type,
        });
        // 用户确认后返回 true
        return true;
    } catch (error) {
        // 用户取消操作时显示取消提示信息
        message({message: '取消操作', type: 'info'});
        // 用户取消后返回 false
        return false;
    }
}

/**
 * 显示消息提示框
 */
const openNotification = ({message, title, type}) => {
    message = message || "";
    title = title || "";
    type = type || "info";

    ElementPlus.ElNotification({message, title, type});
}

/**
 * 复制
 * @returns {Promise<void>}
 */
const copy = async (configText) => {
    try {
        await navigator.clipboard.writeText(configText);
        message({message: "已复制到剪贴板", type: "success"});
    } catch (error) {
        message({message: "复制失败", type: "error"});
        console.error("复制失败:", error);
    }
};

/**
 * 验证表单
 * 根组件调用的方法
 */
const validateForm = (formRef) => {
    if (!formRef) {
        message({message: "表单未初始化", type: "error"})
        return Promise.reject(false);
    }

    return new Promise((resolve) => {
        formRef.validate((valid) => {
            resolve(valid);
        });
    });
};

/**
 * 重置表单
 * 根组件调用的方法
 */
const resetForm = (formRef) => {
    if (!formRef) {
        message({message: "表单未初始化", type: "error"})
        return;
    }

    formRef.resetFields();
};

/**
 * 执行生成请求的通用函数
 */
const executeGenerateRequest = async ({url, data, method, loadingText}) => {
    const loading = ElementPlus.ElLoading.service({
        lock: true,
        text: loadingText,
        background: 'rgba(0, 0, 0, 0.7)'
    });

    // 默认为POST请求
    if (!method) {
        method = "POST";
    }

    try {
        const response = await http({url, method, data});
        if (response.code !== 200) {
            message({message: response.message, type: 'error'});
            return;
        }
        message({message: response.message, type: 'success'});
    } catch (error) {
        console.log(error)
        message({message: '网络错误，请检查连接', type: 'error'});
        openNotification({title: '出错了！', message: error.message, type: 'error'})
    } finally {
        loading.close();
    }
};


// axios 配置
const http = axios.create({
    baseURL: "http://localhost:8077/api",
    timeout: 16000,
    headers: {"Content-Type": "application/json;charset=utf-8"},
});

// 请求拦截器（可选添加）
http.interceptors.request.use(
    (config) => {
        // 可以在这里添加token等
        // const token = localStorage.getItem('token');
        // if (token) {
        //   config.headers.Authorization = `Bearer ${token}`;
        // }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 响应拦截器
http.interceptors.response.use(
    (response) => {
        // 检查配置的响应类型是否为二进制类型, 如果是，直接返回响应对象
        if (
            response.config.responseType === "blob" ||
            response.config.responseType === "arraybuffer"
        ) {
            return response;
        }

        if (response.status === 200) {
            return response.data;
        }

        // 系统出错
        return Promise.reject(response.data.message || "Error");
    },
    (error) => {
        // 异常处理
        if (error.response?.data) {
            return error.response.data;
        }

        return Promise.reject(error.message);
    }
);

// 为了方便使用，可以添加一些快捷方法
http.get = function (url, params = {}) {
    return this({method: "GET", url, params});
};

http.post = function (url, data = {}) {
    return this({method: "POST", url, data});
};

http.put = function (url, data = {}) {
    return this({method: "PUT", url, data});
};

http.delete = function (url) {
    return this({method: "DELETE", url});
};

/**
 * 下载生成的ZIP文件
 * @param response
 */
const downloadZip = (response) => {
    try {
        // 创建下载链接，使用不同的变量名避免冲突
        const blobUrl = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = blobUrl;

        // 获取文件名
        const disposition = response.headers['content-disposition'];
        let filename = disposition || '生成内容.zip';

        link.setAttribute('download', filename);
        document.body.appendChild(link);
        link.click();

        // 清理
        link.parentNode.removeChild(link);
        window.URL.revokeObjectURL(blobUrl);
    } catch (error) {
        message({message: '下载失败，请重试', type: 'error'});
        console.error('下载失败:', error);
    }
};
