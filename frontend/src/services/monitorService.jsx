import client from "./client"

const monitorService = {
    async getMonitors(){
        return await client.get("/monitors");
    },

    async getMonitorById(id){
        return await client.get(`/monitors/${id}`)
    },

    async acceptMonitor() {
        return await client.post("/monitors")
    },
    async getMonitorsByGroup(id){
        return await client.get(`/monitors/group/${id}`)
    },
    async getPendingMonitors(){
        return await client.get('/monitors/pending')
    },
    async acceptMonitor(id){
        return await client.put(`/monitors/accept/${id}`)
    }
}

export default monitorService;