import { useEffect, useState } from 'react';
import { useQueries } from '@tanstack/react-query';
import { DashboardGraph, GroupBar, PageTitle } from '../../components'
import { MdArrowBack, MdBugReport, MdWarning } from 'react-icons/md';
import monitorService from '../../services/monitorService';
import DataTable from 'react-data-table-component';
import { useNavigate } from 'react-router';
import { tableStyle } from "./tableStyleConfig";
import { paginationComponentOptions, columns } from './tableDataConfig';
import logService from '../../services/logService';



function Dashboard() {
    const navigate = useNavigate();
    const [groupId, setGroupId] = useState(null);
    const [groupName, setGroupName] = useState(null);
    const [showMonitorList, setShowMonitorList] = useState(false);
    const [selectedOnline, setSelectedOnline] = useState(false);

    const [onlineMonitorsQuery, offlineMonitorsQuery, onlineMonitorsByGroupQuery, offlineMonitorsByGroupQuery, logsQuery] = useQueries({
        queries : [
            {
                queryKey: ['onlineMonitors', { onlineStatus: true }],
                queryFn: () => monitorService.getMonitors(true),
                enabled: groupId === null
            },
            {
                queryKey: ['offlineMonitors', { onlineStatus: false }],
                queryFn: () => monitorService.getMonitors(false),
                enabled: groupId === null
            },
            {
                queryKey: ['onlineMonitorsByGroup', { groupId, onlineStatus: true }],
                queryFn: () => monitorService.getMonitorsByGroup(groupId, true),
                enabled: groupId != null
            },
            {
                queryKey: ['offlineMonitorsByGroup', { groupId, onlineStatus: false }],
                queryFn: () => monitorService.getMonitorsByGroup(groupId, false),
                enabled: groupId != null
            },
            {
                queryKey: ['logs'],
                queryFn: () => logService.getLogsCountLast30Days()
            }
        ]
    })

    const LoadingDisplay = () => (
        <div className="flex flex-col items-center place-content-center">
            <p className="text-xl">Loading...</p>
        </div>
    );

    const ErrorDisplay = ({ message }) => (
        <div className="flex flex-col items-center place-content-center">
            <p className="text-xl text-red-500">Error: {message}</p>
        </div>
    );

    const DataDisplay = ({ numberMonitors, message }) => (
        <div className="flex flex-col items-center place-content-center">
          <span className="text-6xl pb-2 font-medium">{numberMonitors}</span>
          <span className="text-lg">{message}</span>
        </div>
    );

    const showOnlineMonitors = () => {
        const query = (groupId === null) ? onlineMonitorsQuery : onlineMonitorsByGroupQuery;

        if (query.isLoading) {
            return <LoadingDisplay />;
        } 

        if (query.isError) {
            return <ErrorDisplay message={query.error.message} />;
        }

        return <DataDisplay numberMonitors={query.data.data.length} message={"Online"} />;
    }

    const showOfflineMonitors = () => {
        const query = (groupId === null) ? offlineMonitorsQuery : offlineMonitorsByGroupQuery;

        if (query.isLoading) {
            return <LoadingDisplay />;
        } 

        if (query.isError) {
            return <ErrorDisplay message={query.error.message} />;
        }

        return <DataDisplay numberMonitors={query.data.data.length} message={"Offline"} />;
    }


    const showAllStatusOrSelectedStatus = () => {
        if (showMonitorList) {
            return (
                <div className="w-[25%] h-full">
                    <button onClick={() => {setShowMonitorList(false); setSelectedOnline(false)}} className="flex flex-row h-[10%] text-3xl font-medium items-center pt-2 pl-2">
                        <MdArrowBack className="w-7 h-7 mr-2"/> 
                        <span className="text-xl">Go back</span>
                    </button>
                    <div className="h-[90%] flex flex-row items-center place-content-center">
                        <div className={`hover:opacity-90 h-[55%] w-[45%] rounded-md flex flex-col items-center place-content-center bg-gradient-to-b ${selectedOnline ? "from-[#96D600] from-25% to-[#76A701]" : "from-[#D12E2E] from-46% to-[#A12626] to-90%"}`}>
                            {selectedOnline ? showOnlineMonitors() : showOfflineMonitors()}
                        </div>
                    </div>
                </div>
            )
        }
        else {
            return (
                <div className="w-[50%] h-full">
                    <div className="h-[10%] text-3xl font-medium items-center flex pt-2 pl-2">
                        Monitors
                    </div>
                    <div className="h-[90%] flex flex-row items-center place-content-center">
                        <button onClick={()=>{setShowMonitorList(true); setSelectedOnline(true)}} className="hover:opacity-90 cursor-pointer h-[55%] w-[22%] rounded-md bg-gradient-to-b from-[#96D600] from-25% to-[#76A701] flex flex-col items-center place-content-center mr-[10%]">
                            {showOnlineMonitors()}
                        </button>
                        <button onClick={()=>setShowMonitorList(true)} className="hover:opacity-90 cursor-pointer h-[55%] w-[22%] rounded-md bg-gradient-to-b from-[#D12E2E] from-46% to-[#A12626] to-90% flex flex-col items-center place-content-center mr-[10%]">
                            {showOfflineMonitors()}
                        </button>
                    </div>
                </div>
            )
        }
    }

    const showGraphOrMonitorsList = () => {
        if (showMonitorList){
            let query = null;

            if (groupId === null){
                query = selectedOnline ? onlineMonitorsQuery : offlineMonitorsQuery;
            }
            else {
                query = selectedOnline ? onlineMonitorsByGroupQuery : offlineMonitorsByGroupQuery;
            }

            return (
                <div className="w-[75%] h-full pr-6 pt-4 pb-4 rounded">
                    <DataTable
                    pointerOnHover
                    highlightOnHover
                    pagination
                    paginationComponentOptions={paginationComponentOptions}
                    onRowClicked={(row) => navigate('/monitor/' + row.id, { state: row })}
                    columns={columns}
                    progressPending={query.isLoading}
                    data={query.data?.data || []}
                    theme="solarized"
                    customStyles={tableStyle}/>
                </div>
            )
        }
        else {
            return (
                <div className="w-[50%] h-full flex flex-col place-content-center">
                    <DashboardGraph 
                        data={hourData} 
                        xLabel={"hour"} 
                        xDataKey={"hour"}
                        height={"75%"}
                        title={<><MdWarning className="w-6 h-6 mx-1"/> Downtime (today)</>}
                        linkTo={"/"} 
                        lineDataKey={"numberMonitors"} />
                </div>
            )
        }
    }

    const hourData = [];
    const dayLogsData = [];
    const HOURS_IN_DAY = 24;
    const DAYS_IN_MONTH = 30;
    
    const currentHour = new Date().getTime();
    const oneHourMillis = 60 * 60 * 1000;
    const today = new Date();
    const priorDate = new Date(today.getTime() - (DAYS_IN_MONTH * 24 * oneHourMillis));
    
    for (let index = 0; index <= HOURS_IN_DAY; index++) {
        const hourDate = new Date(currentHour - (index * oneHourMillis));
        hourData.push({
            hour: hourDate.getHours(),
            numberMonitors: index,
        });
    }
    
    for (let index = 1; index <= DAYS_IN_MONTH; index++) {
        const dayDate = new Date(priorDate.getTime() + (index * 24 * oneHourMillis));
        dayLogsData.push({
            day: `${dayDate.getDate()}/${dayDate.getMonth() + 1}`,
            numberLogs: logsQuery.data?.data[index],
        });
    }

    useEffect(() => {
        logService.getLogs("6").then((response) => {
            //console.log(response.data);
        })
    }, []);

    return (
            <div className="flex flex-col h-full">
                <div id="title" className="pt-4 h-[8%]">
                    <PageTitle startTitle={"dashboard"} 
                                middleTitle={{name: showMonitorList ? "dashboard" : "default", groupName, selectedOnline}}
                                endTitle={"default"}/>
                </div>
                <div id="divider" className="flex flex-row overflow-hidden h-[92%]">
                    <div className="w-[30%] flex flex-col">
                        <GroupBar id={groupId} changeId={setGroupId} changeName={setGroupName}/>
                    </div>
                    <div id="content" className="flex flex-col w-full p-3">
                        <div className="h-[49%]">
                            <div className="h-full pt-3 pr-2 pl-2 pb-3 flex flex-row">
                                {showAllStatusOrSelectedStatus()}
                                {showGraphOrMonitorsList()}
                            </div>
                        </div>
                        <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
                        <div className="h-[50%] p-4">
                            <DashboardGraph 
                                    data={dayLogsData} 
                                    xLabel={"day / month"} 
                                    xDataKey={"day"}
                                    height={"90%"}
                                    title={<><MdBugReport className="w-6 h-6 mx-1"/> Logs (past 30 days)</>}
                                    linkTo={"/dashboard/logs"}
                                    lineDataKey={"numberLogs"} />
                        </div>
                    </div>
                </div>
            </div>
    )
}

export default Dashboard;