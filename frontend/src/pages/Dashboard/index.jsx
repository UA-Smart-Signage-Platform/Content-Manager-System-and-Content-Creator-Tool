import { useState } from 'react';
import { GroupBar, PageTitle } from '../../components'

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
                <div id="content" className="flex w-full pr-3 pl-3">
                    Monitors
                </div>
            </div>
        </div>
    )
}

export default Dashboard;