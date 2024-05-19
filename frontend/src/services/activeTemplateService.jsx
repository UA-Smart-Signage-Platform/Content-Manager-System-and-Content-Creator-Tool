import client from "./client";

const activeTemplateService = {
    async addRule(data){
        return await client.post("/templateGroups", data);
    },

    async deleteRule(id){
        return await client.delete(`/templateGroups/${id}`);
    },

    async updateRule(id, data){
        return await client.put(`/templateGroups/${id}`, data);
    },

    async getRule(id){
        return await client.get(`/templateGroups/${id}`);
    }
}

export default activeTemplateService;