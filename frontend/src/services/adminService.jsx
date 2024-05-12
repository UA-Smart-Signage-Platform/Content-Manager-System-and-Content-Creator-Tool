import client from "./client"

const adminService = {
    async getUsers(){
        return await client.get("/users");
    },

    async createUser(user){
        return await client.post("/users",user);
    },

    async deleteUser(id){
        return await client.delete(`/users/${id}`);
    }
}

export default adminService;
