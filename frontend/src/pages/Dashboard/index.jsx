import { useState } from 'react';
import { GroupBar, PageTitle } from '../../components'
import { MdBugReport, MdWarning } from 'react-icons/md';

function Dashboard() {
    const [groupId, setGroupId] = useState(null);

    return (
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"default"} 
                            middleTitle={"default"}
                            endTitle={"default"}/>
            </div>
            <div id="divider" className="flex flex-row h-full">
                <div className="w-[30%]">
                    <GroupBar id={groupId} changeId={setGroupId}/>
                </div>
                <div id="content" className="flex flex-col w-full p-3">
                    <div className="h-[49%]">
                        <div className="h-full pt-3 pr-2 pl-2 pb-3 flex flex-row">
                            <div className="w-[50%] h-full">
                                <div className="h-[10%] text-3xl font-medium items-center flex pt-2 pl-2">
                                    Monitors
                                </div>
                                <div className="h-[90%] flex flex-row items-center place-content-center">
                                    <div className="h-[55%] w-[22%] rounded-md bg-green-400 flex flex-col items-center place-content-center mr-[10%]">
                                        <span className="text-6xl pb-2 font-medium">6</span>
                                        <span className="text-lg">Online</span>
                                    </div>
                                    <div className="h-[55%] w-[22%] rounded-md bg-red flex flex-col items-center place-content-center mr-[10%]">
                                        <span className="text-6xl pb-2 font-medium">3</span>
                                        <span className="text-lg">Offline</span>
                                    </div>
                                </div>
                            </div>
                            <div className="w-[50%] h-full flex flex-col place-content-center">
                                <div className="h-[10%] flex items-center">
                                    <span className="flex flex-row text-lg">
                                        <MdWarning className="w-6 h-6 mx-1"/> 
                                        Downtime (today)
                                    </span>
                                </div>
                                <div className="h-[60%] bg-slate-300">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
                    <div className="h-[50%]">
                        <div className="h-full pt-3 pr-2 pl-2">
                            <div className="h-[10%] flex items-center">
                                <span className="flex flex-row text-lg">
                                    <MdBugReport className="w-6 h-6 mx-1"/> 
                                    Logs (past 30 days)
                                </span>
                            </div>
                            <div className="h-[90%] bg-slate-300">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Dashboard;