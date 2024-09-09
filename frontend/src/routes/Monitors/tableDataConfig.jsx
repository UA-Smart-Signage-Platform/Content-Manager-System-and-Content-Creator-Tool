import { MdGroup, MdInfo, MdMonitor, MdOutlineWarning } from "react-icons/md";

export const columns = [
    {
        name: (                
            <div className="flex flex-row">
                <MdMonitor className="h-6 w-6 mr-2"/> Name
            </div>
        ),
        selector: row => row.name,
        sortable: true,
    },
    {
        name: (                
            <div className="flex flex-row">
                <MdGroup className="h-6 w-6 mr-2"/> Group
            </div>
        ),
        selector: row => !row.group.defaultGroup ? row.group.name : "-----",
        sortable: true,
    },
    {
        name: (                
            <div className="flex flex-row">
                <MdOutlineWarning className="h-6 w-6 mr-2"/> Warnings
            </div>
        ),
        selector: row => row.warnings,
        sortable: true,
    },
    {
        name: (                
            <div className="flex flex-row">
                <MdInfo className="h-6 w-6 mr-2"/> Status
            </div>
        ),
        selector: row => columnStatus(row),
        sortable: true
    }   
];

const columnStatus = (row) => {
    return (
        <div className={`w-[42px] ${row.online ? "bg-primary" : "bg-red" } h-[20px] rounded-xl border-black border-2`} />
    )
} 