import client from "./client";

const activeTemplateService = {
    async addRule(data){
        return await client.post("/templateGroups", data);
    }
}

export default activeTemplateService;