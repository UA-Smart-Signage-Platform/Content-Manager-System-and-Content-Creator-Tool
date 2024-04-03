import client from "./client"

const monitorsGroupService = {
    async getGroups(){
        return await client.get("/groups");
    },

    async getMonitorsByGroup(id){
        return await client.get(`/groups/${id}/screens`)
    }
}

export default monitorsGroupService;