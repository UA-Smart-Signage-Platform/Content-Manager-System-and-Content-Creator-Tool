import client from "./client"

const logService = {
    async getLogs(hours){
        return await client.get(`/logs/backend?hours=${hours}`);
    },

    async getLogsCountLast30Days(){
        return await client.get(`/logs/backend/count`);
    }
}

export default logService;
