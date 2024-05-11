import client from "./client";

const templateservice = {
    async getTemplates() {
        return await client.get("/templates");
    },
    async saveTemplate(template){
        return await client.post("/templates",template)
    }
}

export default templateservice;