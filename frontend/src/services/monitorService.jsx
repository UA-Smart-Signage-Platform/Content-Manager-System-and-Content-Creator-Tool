import client from "./client"

const monitorService = {
    async getMonitors(onlineStatus){
        return await client.get("/monitors", { params: { onlineStatus } });
    },

    async getMonitorById(id){
        return await client.get(`/monitors/${id}`)
    },

    async acceptMonitor() {
        return await client.post("/monitors")
    },

    async getMonitorsByGroup(id, onlineStatus){
        return await client.get(`/monitors/group/${id}`, { params: { onlineStatus } })
    },

    async getPendingMonitors(){
        return await client.get('/monitors/pending')
    },

    async acceptMonitor(id){
        return await client.put(`/monitors/accept/${id}`)
    },

    async updateMonitor(id, monitor){
        return await client.put(`/monitors/${id}`,monitor)
    }
}

export default monitorService;