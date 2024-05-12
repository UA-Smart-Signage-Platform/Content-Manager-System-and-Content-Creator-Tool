import client from "./client";

const activeTemplateService = {
    async addRule(data){
        return await client.post("/templateGroups", data);
    },

    async deleteRule(id){
        return await client.delete(`/templateGroups/${id}`)
    }
}

export default activeTemplateService;