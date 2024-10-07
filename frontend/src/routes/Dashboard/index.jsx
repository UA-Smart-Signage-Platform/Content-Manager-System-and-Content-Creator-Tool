import { useState } from 'react';
import { useQueries } from '@tanstack/react-query';
import { LineDashboardChart, PieDashboardChart, GroupBar, PageTitle } from '../../components'
import { MdArrowBack, MdBugReport, MdOutlineDangerous, MdWarning } from 'react-icons/md';
import monitorService from '../../services/monitorService';
import DataTable from 'react-data-table-component';
import { useNavigate } from 'react-router';
import { tableStyle } from "./tableStyleConfig";
import { paginationComponentOptions, columns } from './tableDataConfig';
import logService from '../../services/logService';
import { logsPerDayData, monitorPerHourData } from './graphsDataConfig';


function Dashboard() {
    const navigate = useNavigate();

    const [groupId, setGroupId] = useState(null);
    const [showMonitorList, setShowMonitorList] = useState(false);
    const [selectedOnline, setSelectedOnline] = useState(false);
    const [showServer, setShowServer] = useState(false)
    const [logsInfoDays, setLogsInfoDays] = useState(30);

    const logsWarningDays = 7;
    const logsErrorDays = 7;

    const [
        onlineMonitorsQuery,
        offlineMonitorsQuery,
        onlineMonitorsByGroupQuery,
        offlineMonitorsByGroupQuery,
        logsInfoCountQuery,
        logsWarningCountQuery,
        logsErrorCountQuery,
        operationLogsErrorCountQuery,
        operationLogsWarningCountQuery,
        operationLogsInfoCountQuery
      ] = useQueries({
        queries: [
          // Queries for online/offline monitors when no group is selected
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
      
          // Queries for online/offline monitors by group
          {
            queryKey: ['onlineMonitorsByGroup', { groupId, onlineStatus: true }],
            queryFn: () => monitorService.getMonitorsByGroup(groupId, true),
            enabled: groupId !== null
          },
          {
            queryKey: ['offlineMonitorsByGroup', { groupId, onlineStatus: false }],
            queryFn: () => monitorService.getMonitorsByGroup(groupId, false),
            enabled: groupId !== null
          },
      
          // Log count queries by severity and number of days
          {
            queryKey: ['logsInfoCount', { logsInfoDays }],
            queryFn: () => logService.getLogsCountByNumberDaysAndSeverity(logsInfoDays, "INFO")
          },
          {
            queryKey: ['logsWarningCount'],
            queryFn: () => logService.getLogsCountByNumberDaysAndSeverity(logsWarningDays, "WARNING")
          },
          {
            queryKey: ['logsErrorCount'],
            queryFn: () => logService.getLogsCountByNumberDaysAndSeverity(logsErrorDays, "ERROR")
          },
      
          // Operation log count queries by severity and number of days
          {
            queryKey: ['operationLogsErrorCount'],
            queryFn: () => logService.getOperationCountByNumberDaysAndSeverity(logsErrorDays, "ERROR")
          },
          {
            queryKey: ['operationLogsWarningCount'],
            queryFn: () => logService.getOperationCountByNumberDaysAndSeverity(logsWarningDays, "WARNING")
          },
          {
            queryKey: ['operationLogsInfoCount'],
            queryFn: () => logService.getOperationCountByNumberDaysAndSeverity(logsInfoDays, "INFO")
          }
        ]
      });

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

    const showMonitorsOrServer = () => {
        const showAllStatusOrSelectedStatus = () => {

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
                        <div className="h-full flex flex-row items-center place-content-center">
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
                        <LineDashboardChart 
                            data={monitorPerHourData()} 
                            xLabel={"hour"} 
                            xDataKey={"hour"}
                            height={"75%"}
                            title={<><MdWarning className="w-6 h-6 mx-1"/> Downtime (last 24 hours)</>}
                            linkTo={"/"} 
                            lineDataKey={"numberMonitors"} />
                    </div>
                )
            }
        }

        if (showServer) {
            return (
                <>
                    <div className="h-[32%] w-full flex">
                        <div className="h-full w-[50%] p-6">
                            <LineDashboardChart 
                                data={logsPerDayData(logsErrorCountQuery, logsErrorDays)} 
                                xLabel={"day / month"} 
                                xDataKey={"day"}
                                height={"90%"}
                                title={<><MdOutlineDangerous className="w-6 h-6 mx-1"/> Error logs (last 7 days)</>}
                                linkTo={"/dashboard/logs/backend"}
                                severity={"ERROR"}
                                lineDataKey={"numberLogs"} />
                        </div>
                        <div className="h-full w-[50%] p-6">
                            <PieDashboardChart 
                                data={operationLogsErrorCountQuery.data?.data} 
                                height={"90%"}
                                title={<>Errors by operation source</>}/>
                        </div>
                    </div>
                    <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
                    <div className="h-[32%] w-full flex">
                        <div className="h-full w-[50%] p-6">
                            <LineDashboardChart 
                                data={logsPerDayData(logsWarningCountQuery, logsWarningDays)} 
                                xLabel={"day / month"} 
                                xDataKey={"day"}
                                height={"90%"}
                                title={<><MdWarning className="w-6 h-6 mx-1"/> Warning logs (last 7 days)</>}
                                linkTo={"/dashboard/logs/backend"}
                                severity={"WARNING"}
                                lineDataKey={"numberLogs"} />
                        </div>
                        <div className="h-full w-[50%] p-6">
                            <PieDashboardChart 
                                data={operationLogsWarningCountQuery.data?.data} 
                                height={"90%"}
                                title={<>Warnings by operation source</>}/>
                        </div>
                    </div>
                    <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
                    <div className="h-[32%] w-full flex">
                        <div className="h-full w-[50%] p-6">
                            <LineDashboardChart 
                                    data={logsPerDayData(logsInfoCountQuery, logsInfoDays)} 
                                    xLabel={"day / month"} 
                                    xDataKey={"day"}
                                    height={"90%"}
                                    title={<><MdBugReport className="w-6 h-6 mx-1"/> Information logs (last 7 days)</>}
                                    linkTo={"/dashboard/logs/backend"}
                                    severity={"INFO"}
                                    lineDataKey={"numberLogs"} />
                        </div>
                        <div className="h-full w-[50%] p-6">
                            <PieDashboardChart 
                                data={operationLogsInfoCountQuery.data?.data} 
                                height={"90%"}
                                title={<>Information by operation source</>}/>
                        </div>
                    </div>
                </>
            )
        }
        else{
            return (
                <>
                    <div className="h-[45%]">
                        <div className="h-full pt-3 pr-2 pl-2 pb-3 flex flex-row">
                            {showAllStatusOrSelectedStatus()}
                            {showGraphOrMonitorsList()}
                        </div>
                    </div>
                    <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
                    <div className="h-[50%] p-4">
                        <LineDashboardChart 
                                data={logsPerDayData(logsInfoCountQuery, logsInfoDays)} 
                                xLabel={"day / month"} 
                                xDataKey={"day"}
                                height={"90%"}
                                title={<><MdBugReport className="w-6 h-6 mx-1"/> Logs (last 30 days)</>}
                                linkTo={"/dashboard/logs/monitors"}
                                lineDataKey={"numberLogs"} />
                    </div>
                </>
            )
        }
    }


    return (
            <div className="flex flex-col h-full">
                <div id="title" className="pt-4 h-[8%]">
                    <PageTitle startTitle={"dashboard"} 
                                middleTitle={{name: showMonitorList ? "dashboard" : "default", selectedOnline}}
                                endTitle={"default"}/>
                </div>
                <div id="divider" className="flex flex-row overflow-hidden h-[92%]">
                    <div className="w-[30%] flex flex-col">
                        <GroupBar id={groupId} changeId={setGroupId} />
                    </div>
                    <div id="content" className="flex flex-col w-full p-3">
                        <div className="h-[4%] w-full text-3xl place-content-center pl-4">
                            <button className="ml-3" onClick={() => {setShowServer(false); setLogsInfoDays(30)}}>Monitors</button>
                            <button className="ml-10" onClick={() => {setShowServer(true); setLogsInfoDays(7)}}>Server</button>
                            <div className="bg-black h-1 w-72"><div className={`h-1 bg-primary ${showServer ? "w-36 ml-36" : "w-36"}`}></div></div>
                        </div>
                        {showMonitorsOrServer()}
                    </div>
                </div>
            </div>
    )
}

export default Dashboard;