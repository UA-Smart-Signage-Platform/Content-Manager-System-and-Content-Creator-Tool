import client from "./client";

const templateservice = {
    async getTemplates() {
        return await client.get("/templates");
    }
}

export default templateservice;