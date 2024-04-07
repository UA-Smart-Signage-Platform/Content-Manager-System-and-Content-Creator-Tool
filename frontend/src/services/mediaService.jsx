import client from "./client"

const mediaService = {
    async getFilesAndDirectories(){
        return await client.get("/files");
    },

    async getFilesByDirectory(id){
        return await client.get(`files/${id}`);
    },

    async createFile(file){
        return await client.post("/files", file);
    },

    async createFolder(folder){
        return await client.post("/files/directory", folder);
    }
}

export default mediaService;