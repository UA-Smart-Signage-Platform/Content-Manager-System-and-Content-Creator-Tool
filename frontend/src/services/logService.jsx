import client from "./client"

const logService = {
    async getLogs(hours){
        return await client.get(`/logs/backend?hours=${hours}`);
    },

    async getLogsCountByNumberDaysAndSeverity(days, severity){
        return await client.get(`/logs/backend/count?days=${days}&severity=${severity}`);
    },

    async getOperationCountByNumberDaysAndSeverity(days, severity){
        return await client.get(`/logs/backend/operation?days=${days}&severity=${severity}`);
    }
}

export default logService;
