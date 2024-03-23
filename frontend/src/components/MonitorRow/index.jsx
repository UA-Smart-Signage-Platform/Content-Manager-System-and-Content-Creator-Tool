import { MdMonitor } from "react-icons/md";

function MonitorRow({Monitor}){
    return(
        <div id="1_Row" className="text-xl mx-3 hover:bg-secondaryLight">
            <div className=" bg-secondary h-[2px] w-full"></div>
            <div className="flex py-4 px-1">
                <span className="flex items-center gap-2 w-[40%] mr-2"><MdMonitor className=" size-7"/>Monitor Room 4.02.13</span>
                <span className=" text-center">DETI</span>
                <span className="ml-auto w-[10%] text-center">0</span>
                <div className="w-[10%] flex items-center justify-center">
                    <div className=" w-[40%] bg-primary h-[20px] rounded-xl border-black border-2">
                    </div>
                </div>
                <span className="w-[10%] text-right">192.168.1.112</span>
            </div>
        </div>
    )
}

export default MonitorRow;