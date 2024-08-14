import { useQueries, useQueryClient } from '@tanstack/react-query';
import { GroupBar, PageTitle } from '../../components';
import { useState } from 'react';
import { MdArrowBack } from 'react-icons/md';
import DataTable from 'react-data-table-component';

function Logs() {
    const queryClient = useQueryClient();
    const [groupId, setGroupId] = useState(null);
    const [groupName, setGroupName] = useState(null);
    const [selectedOnline, setSelectedOnline] = useState(false);

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
                            <button onClick={() => {}} className="flex flex-row text-3xl font-normal items-center pl-2">
                                <MdArrowBack className="w-7 h-7 mr-2"/> 
                                <span className="text-xl">Go back</span>
                            </button>
                        </div>
                        <div className="w-full h-full pt-2 flex">
                            <div className="ml-auto flex items-center">
                                <button onClick={() => {}} className="flex flex-row text-3xl font-normal items-center">
                                    <span className="text-xl">BUTTON WILL BE HERE</span>
                                </button>
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