import client from "./client"

const monitorsGroupService = {
    async getGroups(){
        return await client.get("/groups");
    },
}

export default monitorsGroupService;