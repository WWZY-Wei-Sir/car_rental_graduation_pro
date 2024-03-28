import axios from 'axios';
import {ElMessage} from 'element-plus';

const request = axios.create({
    baseURL: "http://localhost:8090",
    timeout: 10000
});

// axios实例拦截响应
request.interceptors.response.use(
    (response) => {
        let res = response.data;

        // 如果是返回的文件
        if (response.config.responseType === "blob") {
            return res;
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === "string") {
            res = res ? JSON.parse(res) : res;
        }

        return res;
    },
    // 请求失败
    (error: any) => {
        const response = error;
        if (response) {
            return Promise.reject(response.data);
        } else {
            ElMessage.error('网络连接异常,请稍后再试!');
            localStorage.removeItem('user_info');
        }
    }
);

// axios实例拦截请求
request.interceptors.request.use(
    (config) => {
        config.headers["Content-Type"] = "application/json;charset=utf-8";

        const user_info = localStorage.getItem('user_info');
        if (user_info) {
            config.headers["user_info"] = JSON.parse(JSON.stringify(user_info));
        }

        return config;
    },
    (error:any) => {
        ElMessage.error('网络连接异常,请稍后再试!');
        return Promise.reject(error);
    }
)

export default request;