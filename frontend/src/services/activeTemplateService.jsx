import client from "./client";

const activeTemplateService = {
    async changeActiveTemplate(data){
        return await client.put("/templateGroups/set", data);
    },
    async addActiveTemplate(data){
        return await client.post("/templateGroups", data);
    }
}

export default activeTemplateService;