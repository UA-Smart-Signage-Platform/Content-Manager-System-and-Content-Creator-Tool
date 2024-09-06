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
    },
    
    async deleteGroup(id){
        return await client.delete(`/groups/${id}`)
    },
    
    async getNonDefaultGroups(){
        return await client.get("/groups/ignoreDefault");
    },

    async getGroup(id){
        return await client.get(`/groups/${id}`)
    }
}

export default monitorsGroupService;