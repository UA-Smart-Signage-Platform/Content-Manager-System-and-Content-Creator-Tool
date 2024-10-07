import { useQueries, useQueryClient } from '@tanstack/react-query';
import { GroupBar, PageTitle } from '../../components';
import { useState } from 'react';
import { MdArrowBack, MdKeyboardArrowDown } from 'react-icons/md';
import DataTable from 'react-data-table-component';
import { useLocation, useNavigate } from 'react-router-dom';
import { MdAccessTime } from 'react-icons/md';
import logService from '../../services/logService';
import { columns, ExpandedComponent } from './tableDataConfig';
import { conditionalRowStyles, tableStyle } from './tableStyleConfig';

function ServerLogs() {
    const { state } = useLocation();
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const [groupId, setGroupId] = useState(null);
    const [severityFilter, setSeverityFilter] = useState(state.severity ? state.severity : "");
    const [groupName, setGroupName] = useState(null);
    const [selectedTime, setSelectedTime] = useState(1);
    const [selectedOnline, setSelectedOnline] = useState(false);
    const [logPerSeverity, setlogPerSeverity] = useState({info: 0, warning: 0, error: 0});


    const [backendLogsQuery] = useQueries({
        queries : [
            {
                queryKey: ['backendLogs', selectedTime],
                queryFn: () => logService.getLogs(selectedTime).then((response) => {
                    setlogPerSeverity(countSeverities(response.data));
                    return response;
                })
            }
        ]
    });

    const countSeverities = (logs) => {
        return logs.reduce(
          (counts, log) => {
            if (log.severity === "INFO") counts.info++;
            if (log.severity === "WARNING") counts.warning++;
            if (log.severity === "ERROR") counts.error++;
            return counts;
          },
          { info: 0, warning: 0, error: 0 }
        );
    };

    const filteredLogs = backendLogsQuery.data?.data.filter(
		log => log.severity && log.severity.toLowerCase().includes(severityFilter.toLowerCase()),
	);

    return(
        <div className="flex flex-col h-full">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"dashboard"} 
                            middleTitle={{name: "default"}}
                            endTitle={"default"}/>
            </div>
            <div id="divider" className="flex flex-row overflow-hidden h-[92%]">
                <div className="w-[30%] flex flex-col">
                    <GroupBar id={groupId} changeId={setGroupId} changeName={setGroupName}/>
                </div>
                <div id="content" className="flex flex-col w-full p-3">
                    <div className="h-[5%] w-full flex">
                        <div className="w-[50%] h-full pt-2 ">
                            <button onClick={() => navigate("/dashboard")} className="flex flex-row text-3xl font-normal items-center pl-2">
                                <MdArrowBack className="w-7 h-7 mr-2"/> 
                                <span className="text-xl">Go back</span>
                            </button>
                        </div>
                        <div className="w-full h-full pt-2 flex">
                            <div className="ml-auto flex items-center bg-secondaryMedium rounded-lg pl-2">
                                <MdAccessTime size={32} />
                                <div className="relative">
                                    <select onChange={(e) => setSelectedTime(e.target.value)} defaultValue={selectedTime}
                                        className="h-full w-full bg-secondaryMedium pl-3 pr-11 appearance-none rounded-lg flex flex-row text-xl font-medium items-center">
                                        <option value="1">Last hour</option>
                                        <option value="3">Last 3 hours</option>
                                        <option value="6">Last 6 hours</option>
                                        <option value="12">Last 12 hours</option>
                                        <option value="24">Last 24 hours</option>
                                        <option value="48">Last 2 days</option>
                                        <option value="168">Last 7 days</option>
                                        <option value="720">Last 30 days</option>
                                    </select>
                                    <div className="absolute top-0 right-0 h-full flex items-center pointer-events-none pr-3">
                                        <MdKeyboardArrowDown className="ml-2 mt-1" size={32}/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="h-[95%] w-full pt-2 pl-4">
                        <div className="h-full w-full rounded-md bg-secondaryLight">
                            <div className="h-[26%] w-full pt-3 pb-3 pr-4 pl-4">
                                <div className="h-full w-full flex justify-between items-center">
                                    <button onClick={() => {setSeverityFilter(severityFilter === "ERROR" ? "" : "ERROR")}} 
                                            className={`h-full w-[32.5%] bg-[#D12E2E] rounded-md hover:opacity-90 ${severityFilter === "ERROR" || severityFilter === "" ? "" : "opacity-70"}`}>
                                        <span className="flex flex-col h-full w-full text-white items-center justify-end pb-2">
                                            <span className="text-8xl font-medium">{logPerSeverity.error}</span>
                                            <span className="text-xl">errors</span>
                                        </span>
                                    </button>
                                    <button onClick={() => {setSeverityFilter(severityFilter === "WARNING" ? "" : "WARNING")}} 
                                            className={`h-full w-[32.5%] bg-[#DC8331] rounded-md hover:opacity-90 ${severityFilter === "WARNING" || severityFilter === "" ? "" : "opacity-70"}`}>
                                        <span className="flex flex-col h-full w-full text-white items-center justify-end pb-2">
                                            <span className="text-8xl font-medium">{logPerSeverity.warning}</span>
                                            <span className="text-xl">warnings</span>
                                        </span>
                                    </button>
                                    <button onClick={() => {setSeverityFilter(severityFilter === "INFO" ? "" : "INFO")}} 
                                            className={`h-full w-[32.5%] bg-[#A1A1A1] rounded-md hover:opacity-90 ${severityFilter === "INFO" || severityFilter === "" ? "" : "opacity-70"}`}>
                                        <span className="flex flex-col h-full w-full text-white items-center justify-end pb-2">
                                            <span className="text-8xl font-medium">{logPerSeverity.info}</span>
                                            <span className="text-xl">informative</span>
                                        </span>
                                    </button>
                                </div>
                            </div>
                            <div className="w-full h-[74%] pr-6 pl-6 pt-2 pb-3 rounded">
                                <div className="bg-white rounded-lg w-full h-full">
                                    <DataTable
                                        expandableRows
                                        expandableRowsComponent={ExpandedComponent}
                                        fixedHeader
                                        fixedHeaderScrollHeight={"100%"}
                                        defaultSortFieldId={"timestamp"}
                                        defaultSortAsc={false}
                                        progressPending={backendLogsQuery.isPending}
                                        columns={columns}
                                        data={filteredLogs || []}
                                        theme="solarized"
                                        customStyles={tableStyle}
                                        conditionalRowStyles={conditionalRowStyles}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default ServerLogs;