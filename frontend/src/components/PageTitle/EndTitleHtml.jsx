import { MdOutlineSearch, MdAdd } from "react-icons/md";
import Portals from "../Portals"
import { useState } from "react";

function EndTitleHtml( { page } ) {
    const [showPortal, setShowPortal] = useState(false);
    
    if (page === "default")
        return (
            <div className="flex border-2 border-searchButton rounded-md drop-shadow-md">
                <input className="z-10 bg-secondaryLight rounded-l-sm pl-2 pr-2 text-textcolor"
                        placeholder="Search for monitor..." 
                        autocomplete="off"/>
                <button className="rounded-r-sm bg-searchButton">
                    <MdOutlineSearch className="h-6 w-6 mr-1 ml-1"/>
                </button>
            </div>
        )
    if (page === "monitors")
        return(
            <div className="flex border-searchButton rounded-md drop-shadow-md mr-2">
                <button onClick={() => setShowPortal(true)} className="rounded-md bg-secondaryLight flex flex-row p-1 pr-2">
                    <MdAdd className="h-6 w-6 mr-1 ml-1"/>
                    <span>Pending Monitors</span>
                </button>
                <Portals page="monitors" showPortal={showPortal} setShowPortal={setShowPortal}/>
            </div>
        )
    if (page === "...")
        return(
            <div></div>
        )
}
export default EndTitleHtml