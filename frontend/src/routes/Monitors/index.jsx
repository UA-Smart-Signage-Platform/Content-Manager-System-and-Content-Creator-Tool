import { useEffect, useState } from "react";
import { PageTitle, GroupBar } from "../../components";
import monitorService from "../../services/monitorService"
import { useNavigate } from "react-router-dom";
import DataTable from 'react-data-table-component';

function Monitors(){
    const [monitors, setMonitors] = useState([]);
    const [groupId, setGroupId] = useState(null);
    const [updater, setUpdater] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (groupId === null){
            monitorService.getMonitors().then((response) => {
                setMonitors(response.data);
            })
        }
        else{
            monitorService.getMonitorsByGroup(groupId).then((response) => {
                setMonitors(response.data);
            })
        }

    }, [groupId, updater]);


    const customStyles = {
        headRow: {
            style: {
                fontSize: '20px',
                borderBottomWidth: '2px'
            },
        },
        rows: {
            style: {
                fontSize: '16px'
            },
        },
        headCells: {
            style: {
                paddingLeft: '8px',
                paddingRight: '8px'
            },
        },
        cells: {
            style: {
                paddingLeft: '8px',
                paddingRight: '8px'
            },
        },
    };

    const columns = [
        {
            name: 'Name',
            selector: row => row.name,
            sortable: true,
        },
        {
            name: 'Group',
            selector: row => !row.group.madeForMonitor ? row.group.name : "-----",
            sortable: true,
        },
        {
            name: 'Warnings',
            selector: row => row.warnings,
            sortable: true,
        },
        {
            name: 'Status',
            selector: row => <div className={`w-[42px] ${row.online ? "bg-primary" : "bg-red" } h-[20px] rounded-xl border-black border-2`} />,
            sortable: true
        }   
    ];
    

    return(
        <div className="flex flex-col h-full">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"monitors"} 
                            middleTitle={"dashboard"}
                            endTitle={"monitors"}
                            updater={updater}
                            setUpdater={setUpdater}/>
            </div>
            <div id="divider" className="flex flex-row overflow-hidden h-[92%]">
                <div className="w-[30%] flex flex-col">
                    <GroupBar id={groupId} changeId={setGroupId} page={"monitors"}/>
                </div>
                <DataTable className="p-3" 
                    pointerOnHover
                    highlightOnHover
                    onRowClicked={(row) => navigate('/monitor/' + row.id, { state: row })}
                    columns={columns}
                    data={monitors}
                    theme="solarized"
                    customStyles={customStyles}
                />
            </div>
        </div>
    )
}

export default Monitors;