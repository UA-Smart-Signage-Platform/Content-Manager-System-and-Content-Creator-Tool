import axios from "axios";

const client = axios.create({
    baseURL: 'http://localhost:8080/api/',
    timeout: 5000
});

// Add a request interceptor
client.interceptors.request.use(
    (config) => {
        const tokenString = localStorage.getItem('access_token');
        const tokenObject = tokenString ? JSON.parse(tokenString) : null;

        if (tokenObject) {
            config.headers['Authorization'] = `Bearer ${tokenObject.value}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default client;
