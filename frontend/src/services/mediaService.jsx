import client from "./client"

const mediaService = {
    async getFilesAtRootLevel(){
        return await client.get("files/directory/root");
    },

    async getFileOrDirectoryById(id){
        return await client.get(`files/${id}`);
    },

    async createFile(file){
        return await client.post("files", file);
    },

    async createFolder(folder){
        return await client.post("files/directory", folder);
    },

    async deleteFileOrFolder(id){
        return await client.delete(`files/${id}`);
    },

    async editFileOrFolder(id, name){
        return await client.put(`files/${id}`, name);
    },
}

export default mediaService;