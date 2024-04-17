import { MdMonitor } from "react-icons/md";

function MonitorRow({monitor, index}){
    return(
        <div id="1_Row" className="cursor-pointer text-xl mx-3 hover:bg-secondaryLight">
            <div className=" bg-secondary h-[2px] w-full"></div>
            <div className="flex py-4 px-1">
                <span className="flex items-center gap-2 w-[40%] mr-2"><MdMonitor className=" size-7"/>{monitor.name}</span>
                <span className=" text-center">{monitor.group.name}</span>
                <span className="ml-auto w-[10%] text-center">0</span>
                <div className="w-[10%] flex items-center justify-center">
                    <div className=" w-[40%] bg-primary h-[20px] rounded-xl border-black border-2">
                    </div>
                </div>
                <span className="w-[10%] text-right">{monitor.ip}</span>
            </div>
        </div>
    )
}

export default MonitorRow;