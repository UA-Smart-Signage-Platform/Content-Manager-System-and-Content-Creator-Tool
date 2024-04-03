import client from "./client"

const monitorsGroupService = {
    async getGroups(){
        return await client.get("/groups");
    },

    async getGroupById(id){
        return await client.get(`/groups/${id}`)
    }
}

export default monitorsGroupService;