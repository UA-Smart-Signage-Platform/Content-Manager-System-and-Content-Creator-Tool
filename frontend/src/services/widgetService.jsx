import client from "./client";

const widgetService = {
    async getWidgets(){
        return client.get("/widgets");
    }
}

export default widgetService;