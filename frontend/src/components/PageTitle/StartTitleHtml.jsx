import { MdAccountCircle,MdSettings } from "react-icons/md";


function StartTitleHtml( {page} ){
    if (page === "dashboard")
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
                <span className="font-bold text-4xl">Monitors</span>
        )
    if (page === "...")
        return(
            <div></div>
        )
}

export default StartTitleHtml