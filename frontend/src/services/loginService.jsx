import client from "./client"

const loginService = {
    async login(username, password){
        return await client.post("login", {username, password});
    },

    async getInfo(){
        return await client.get("login/info");
    },

    async changePassword(username, currentPassword, newPassword){
        return await client.put("login/change-password", {username, currentPassword, newPassword});
    }
}


export default loginService;