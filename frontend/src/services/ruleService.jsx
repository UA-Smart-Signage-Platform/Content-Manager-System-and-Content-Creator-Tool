import client from "./client";

const ruleService = {
    async getRulesByGroupId(id) {
        return await client.get(`/rules/group/${id}`, id);
    },

    async addRule(rule) {
        return await client.post("/rules", rule);
    }
}

export default ruleService;