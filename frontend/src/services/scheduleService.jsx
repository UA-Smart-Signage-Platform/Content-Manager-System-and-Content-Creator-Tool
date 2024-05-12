import client from "./client";

const scheduleService = {
    async updateSchedule(data) {
        return await client.put("/schedules", data);
    }
}

export default scheduleService;