import client from "./client";

const templateservice = {
    async getTemplates() {
        return await client.get("/templates");
    },
    async saveTemplate(template){
        return await client.post("/templates",template)
    },
    async getTemplate(id){
        return await client.get(`/templates/${id}`);
    },
    async deleteTemplateWidget(id){
        return await client.delete(`/templateWidgets/${id}`);
    },
    async deleteTemplate(id){
        return await client.delete(`/templates/${id}`)
    }
}

export default templateservice;