

function MiddleTitleHtml( {page} ){
    switch (page.name) {
        case "default":
            return (
                <div></div>
            );
        case "dashboard":
            return (
                <span className="font-medium text-2xl">
                    Currently viewing {page.groupName === null ? "all" : page.groupName} {page.selectedOnline ? "Online" : "Offline"} Monitors
                </span>
            );
        case "logs":
            return (
                <span className="font-medium text-2xl">
                    Currently viewing {page.groupName === null ? "all" : page.groupName} {page.selectedOnline ? "Online" : "Offline"} Monitors Logs
                </span>
            );
        default:
            return (
                <div></div>
            );
    }
}
export default MiddleTitleHtml