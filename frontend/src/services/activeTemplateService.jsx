import client from "./client";

const activeTemplateService = {
    async changeActiveTemplate(data){
        return await client.put("/templateGroups/set", data);
    }
}

export default activeTemplateService;