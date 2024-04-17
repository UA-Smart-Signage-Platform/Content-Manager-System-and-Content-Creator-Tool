import { MdAccountCircle, MdInfoOutline, MdMonitor } from "react-icons/md";


function StartTitleHtml( {page} ){
    if (page === "default")
        return (
        <div className="flex">
            <MdAccountCircle className="h-6 w-6"/>
            <span className="ml-2">
                Welcome Nuno
            </span>
        </div>
        )
    if (page === "monitors")
        return(
                <span className="font-bold text-3xl">Monitors</span>
        )
    if (page === "media")
        return(
            <span className="font-bold text-3xl">Media</span>
        )
    if (page === "schedule")
        return(
            <span className="font-bold text-3xl flex flex-row place-content-end items-end">
                Schedule
                <MdInfoOutline className="ml-2 w-6 cursor-pointer"/>
            </span>
    )
    if (page === "monitor")
        return(
            <span className="font-bold text-3xl flex flex-row place-content-end items-end">
                <MdMonitor className="mx-2 text-4xl"/>
                Monitor
            </span>
    )
}

export default StartTitleHtml