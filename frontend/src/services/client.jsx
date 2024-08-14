import axios from "axios";
import { useUserStore } from "../stores/useUserStore";

const client = axios.create({
    baseURL: import.meta.env.VITE_APP_API_URL + "/api/",
    timeout: 5000,
})

// Add a request interceptor
client.interceptors.request.use(
    (config) => {
        const token =  useUserStore.getState().token()
        
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default client;
