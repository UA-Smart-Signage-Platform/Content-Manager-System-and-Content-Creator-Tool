import { MdOutlineSearch, MdAdd } from "react-icons/md";
import PendingMonitorsModal from "../Portals/PendingMonitorsModal";
import { useState } from "react";
import PropTypes from 'prop-types';

function EndTitleHtml( { page, updater, setUpdater } ) {
    const [showPortal, setShowPortal] = useState(false);
    
    if (page === "default")
        return (
            <div className="flex border-2 border-searchButton rounded-md drop-shadow-md">
                <input className="z-10 bg-secondaryLight rounded-l-sm pl-2 pr-2 text-textcolor"
                        placeholder="Search for monitor..." 
                        autoComplete="off"/>
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
                <PendingMonitorsModal showPortal={showPortal} setShowPortal={setShowPortal} monitorListUpdate={updater} setMonitorListUpdate={setUpdater}/>
            </div>
        )
    if (page === "...")
        return(
            <div></div>
        )
}

EndTitleHtml.propTypes = {
    page: PropTypes.string.isRequired,
    updater: PropTypes.bool,
    setUpdater: PropTypes.func
}

export default EndTitleHtml