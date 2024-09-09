import client from "./client";

const ruleService = {
    async getRulesByGroupId(id) {
        return await client.get(`/rules/group/${id}`, id);
    }
}

export default ruleService;