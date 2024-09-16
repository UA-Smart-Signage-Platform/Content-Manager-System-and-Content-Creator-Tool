import client from "./client";

const ruleService = {
    async getRulesByGroupId(id) {
        return await client.get(`/rules/group/${id}`);
    },

    async addRule(rule) {
        return await client.post("/rules", rule);
    },

    async getRuleById(id) {
        return await client.get(`/rules/${id}`)
    },

    async updateRule(id, rule) {
        return await client.put(`/rules/${id}`, rule)
    },

    async deleteRule(id) {
        return await client.delete(`/rules/${id}`)
    }
}

export default ruleService;