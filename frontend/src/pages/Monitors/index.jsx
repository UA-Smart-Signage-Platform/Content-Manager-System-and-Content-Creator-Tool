import { useEffect, useState } from "react";
import { PageTitle,GroupBar,MonitorRow } from "../../components";
import { ReactComponent as MonitorsIcon } from "../../static/monitors.svg"
import monitorService from "../../services/monitorService"
import monitorsGroupService from "../../services/monitorsGroupService"

function Monitors(){

    const [monitors, setMonitors] = useState({});
    let groupId = document.getElementById("selected").getAttribute("group-id");


    useEffect(() => {
        if (groupId == 0){
            monitorService.getMonitors().then((monitorsData) => {
                setMonitors(monitorsData.data);

            })
        }
        else{
            monitorsGroupService.getMonitorsByGroup(groupId).then((monitorsData) => {
                setMonitors(monitorsData.data);

            })
        }

    }, [groupId]);

    console.log(monitors);

    return(
        <div className="flex flex-col h-full">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"monitors"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex flex-row overflow-hidden h-[92%]">
                <div className="w-[30%] flex flex-col">
                        <GroupBar/>
                </div>
                <div id="content" className="w-full pr-3 pl-3 flex flex-col">
                    <div id="Title_Row" className="text-2xl flex px-3 pt-3 justify-items-center text-center">
                        <span className="flex gap-2 items-center w-[40%]"><MonitorsIcon className="size-8"/>All Monitors</span>
                        <span className="text-center">Group</span>
                        <span className="ml-auto w-[10%]">Warnings</span>
                        <span className="w-[10%]">Status</span>
                        <span className="w-[10%] text-right">IP</span>
                    </div>
                    <div className="overflow-scroll">
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Monitors;