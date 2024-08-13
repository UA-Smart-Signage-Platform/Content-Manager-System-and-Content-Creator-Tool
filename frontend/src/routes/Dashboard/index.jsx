import { useState } from 'react';
import { useQueries, useQueryClient } from '@tanstack/react-query';
import { DashboardGraph, GroupBar, PageTitle } from '../../components'
import { MdArrowBack, MdBugReport, MdRemoveRedEye, MdWarning } from 'react-icons/md';
import monitorService from '../../services/monitorService';
import DataTable from 'react-data-table-component';



function Dashboard() {
    const queryClient = useQueryClient();
    const [groupId, setGroupId] = useState(null);
    const [showMonitorList, setShowMonitorList] = useState(false);
    const [selectedOnline, setSelectedOnline] = useState(false);

    const [onlineMonitorsQuery, offlineMonitorsQuery, onlineMonitorsByGroupQuery, offlineMonitorsByGroupQuery] = useQueries({
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

    const customStyles = {
        head: {
            style: {
                fontWeight: 400,
            },
        },
        headRow: {
            style: {
                fontSize: '16px',
                minHeight: '32px',
                backgroundColor: "#C1C1C1",
                borderradius: '20px',
                borderBottomWidth: '0px'
            },
        },
        headCells: {
            style: {
                paddingLeft: '16px',
            },
        },
        rows: {
            style: {
                fontSize: '16px',
                minHeight: '32px',
                backgroundColor: "#E9E9E9",
                '&:not(:last-of-type)': {
                    borderBottomStyle: 'solid',
                    borderBottomWidth: '1px',
                    borderBottomColor: "#A7A8AA",
                },
            },
        },
        cells: {
            style: {
                paddingLeft: '10px',
                paddingRight: '0px',
                wordBreak: 'break-word',
            }
        }
    };

    const paginationComponentOptions = {
        noRowsPerPage: true,
        selectAllRowsItem: false,
    };

    const columns = [
        {
            name: (                
                <div className="flex flex-row">
                     Name
                </div>
            ),
            selector: row => <div className='flex items-center'>
                                <MdRemoveRedEye className="border h-5 w-5 rounded border-black mr-3" /> 
                                <span>{row.name}</span> 
                                <span className=" ml-7">/</span>
                            </div>,
            sortable: true,
        },
        {
            name: (                
                <div className="flex flex-row">
                    Group
                </div>
            ),
            selector: row => !row.group.madeForMonitor ? row.group.name : "-----",
            sortable: true,
        },
        {
            name: (                
                <div className="flex flex-row">
                     Warns (5 days)
                </div>
            ),
            selector: row => row.warnings
        },
        {
            name: (                
                <div className="flex flex-row">
                     Online for
                </div>
            ),
            selector: row => row.online
        }   
    ];

    const showGraphOrMonitorsList = () => {
        if (showMonitorList){
            return (
                <div className="w-[75%] h-full pr-6 pt-4 pb-4 rounded">
                    <DataTable
                    pointerOnHover
                    highlightOnHover
                    pagination
                    paginationComponentOptions={paginationComponentOptions}
                    onRowClicked={(row) => navigate('/monitor/' + row.id, { state: row })}
                    columns={columns}
                    data={(groupId === null ? 
                            (selectedOnline ? onlineMonitorsQuery.data.data : offlineMonitorsQuery.data.data) 
                            : 
                            (selectedOnline ? onlineMonitorsByGroupQuery.data.data : offlineMonitorsByGroupQuery.data.data))}
                    theme="solarized"
                    customStyles={customStyles}/>
                </div>
            )
        }
        else {
            return (
                <div className="w-[50%] h-full flex flex-col place-content-center">
                    <DashboardGraph 
                    data={data1} 
                    xLabel={"hour"} 
                    yLabel={"monitors"}
                    height={"75%"}
                    title={<><MdWarning className="w-6 h-6 mx-1"/> Downtime (today)</>}
                    linkTo={"/"} />
                </div>
            )
        }
    }



    const data1 = [];
    const data2 = [];
    for (let index = 0; index <= 24; index++) {
        data1.push( 
            {
                "hour": index,
                "monitor": index,
            }
            );
    };
    for (let index = 1; index <= 30; index++) {
        data2.push( 
            {
                "day": index + "/4",
                "monitor": index,
            }
            );
    }

    return (
        
            <div className="flex flex-col h-full">
                <div id="title" className="pt-4 h-[8%]">
                    <PageTitle startTitle={"dashboard"} 
                                middleTitle={"default"}
                                endTitle={"default"}/>
                </div>
                <div id="divider" className="flex flex-row overflow-hidden h-[92%]">
                    <div className="w-[30%] flex flex-col">
                        <GroupBar id={groupId} changeId={setGroupId} />
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
                                    data={data2} 
                                    xLabel={"day"} 
                                    yLabel={"monitors"}
                                    height={"90%"}
                                    title={<><MdBugReport className="w-6 h-6 mx-1"/> Logs (past 30 days)</>}
                                    linkTo={"/"} />
                        </div>
                    </div>
                </div>
            </div>
    )
}

export default Dashboard;