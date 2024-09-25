const HOURS_IN_DAY = 24;
const DAYS_IN_MONTH = 30;
const oneHourMillis = 60 * 60 * 1000;

export const monitorPerHourData = () => {
    const hourMonitorData = [];
    const currentHour = new Date().getTime();

    for (let index = 0; index <= HOURS_IN_DAY; index++) {
        const hourDate = new Date(currentHour - (index * oneHourMillis));
        hourMonitorData.push({
            hour: hourDate.getHours(),
            numberMonitors: index,
        });
    }

    return hourMonitorData;
}

export const logsPerDayData = (logsQuery) => {
    const dayLogsData = [];
    const today = new Date();
    const priorDate = new Date(today.getTime() - (DAYS_IN_MONTH * 24 * oneHourMillis));

    for (let index = 1; index <= DAYS_IN_MONTH; index++) {
        const dayDate = new Date(priorDate.getTime() + (index * 24 * oneHourMillis));
        dayLogsData.push({
            day: `${dayDate.getDate()}/${dayDate.getMonth() + 1}`,
            numberLogs: logsQuery.data?.data[index],
        });
    }

    return dayLogsData;
}