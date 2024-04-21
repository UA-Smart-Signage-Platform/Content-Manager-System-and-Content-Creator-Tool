import client from "./client"

const loginService = {
    async login(username, password){
        return await client.post("login", {username, password});
    },

    async getInfo(){
        return await client.get("login/info");
    }
}


export default loginService;