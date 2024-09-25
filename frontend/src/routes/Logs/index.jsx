import { useQueries, useQueryClient } from '@tanstack/react-query';
import { GroupBar, PageTitle } from '../../components';
import { useState } from 'react';
import { MdArrowBack, MdKeyboardArrowDown } from 'react-icons/md';
import DataTable from 'react-data-table-component';
import { useNavigate } from 'react-router-dom';
import { MdAccessTime } from 'react-icons/md';
import logService from '../../services/logService';

function Logs() {
    const queryClient = useQueryClient();
    const [groupId, setGroupId] = useState(null);
    const [groupName, setGroupName] = useState(null);
    const [selectedTime, setSelectedTime] = useState(1);
    const [selectedOnline, setSelectedOnline] = useState(false);
    const navigate = useNavigate();

    const [backendLogsQuery] = useQueries({
        queries : [
            {
                queryKey: ['backendLogs', selectedTime],
                queryFn: () => logService.getLogs(selectedTime)
            }
        ]
    })

    return(
        <div className="flex flex-col h-full">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"dashboard"} 
                            middleTitle={{name: "logs" , groupName, selectedOnline}}
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
                                    <button onClick={() => {}} className="h-full w-[32.5%] bg-red rounded-md hover:opacity-90">
                                        <span className="flex flex-col h-full w-full text-white items-center justify-end pb-2">
                                            <span className="text-8xl font-medium">12</span>
                                            <span className="text-xl">errors</span>
                                        </span>
                                    </button>
                                    <button className="h-full w-[32.5%] bg-orange rounded-md hover:opacity-90">
                                        <span className="flex flex-col h-full w-full text-white items-center justify-end pb-2">
                                            <span className="text-8xl font-medium">13</span>
                                            <span className="text-xl">warnings</span>
                                        </span>
                                    </button>
                                    <button className="h-full w-[32.5%] bg-grey rounded-md hover:opacity-90">
                                        <span className="flex flex-col h-full w-full text-white items-center justify-end pb-2">
                                            <span className="text-8xl font-medium">1930</span>
                                            <span className="text-xl">informative</span>
                                        </span>
                                    </button>
                                </div>
                            </div>
                            <div className="w-full h-[74%] pr-5 pl-5 pt-2 pb-3 rounded">
                                <div className="bg-white w-full h-full">
                                    DATA TABLE WILL BE HERE
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default Logs;