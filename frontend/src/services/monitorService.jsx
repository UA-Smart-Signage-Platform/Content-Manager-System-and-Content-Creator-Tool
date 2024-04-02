import client from "./client"

const monitorService = {
    async getMonitors(){
        return await client.get("/monitors");
    },

    async getMonitorById(id){
        return await client.get(`/monitors/${id}`)
    },

    async getMonitorsByGroup(group) {
        return await client.get(`/monitors/group/${group}`)
    },

    async acceptMonitor() {
        return await client.post("/monitors")
    }
}

export default monitorService;