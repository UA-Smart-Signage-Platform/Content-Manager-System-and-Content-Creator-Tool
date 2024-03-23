import { PageTitle,GroupBar,MonitorRow } from "../../components";
import {ReactComponent as MonitorsIcon} from "../../static/monitors.svg"

function Monitors(){
    
    return(
        <div className="flex flex-col flex-1 max-h-full">
            <div id="title" className="mt-4 h-[8%]">
                <PageTitle startTitle={"monitors"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex flex-row overflow-hidden">
                <div className="w-[30%] flex flex-col">
                    <div className=" grow min-h-0">
                        <GroupBar/>
                    </div>
                </div>
                <div id="content" className="w-full pr-3 pl-3 flex flex-col flex-1">
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
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
                        <MonitorRow/>
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