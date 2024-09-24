import client from "./client"

const logService = {
    async getLogs(hours){
        return await client.get(`/logs/backend?hours=${hours}`);
    }
}

export default logService;
