import client from "./client"

const monitorsGroupService = {
    async getGroups(){
        return await client.get("/groups");
    },
    async createGroup(group){
        return await client.post("/groups",group)
    },
    async updateGroup(id,group){
        return await client.put(`/groups/${id}`,group)
    }
}

export default monitorsGroupService;