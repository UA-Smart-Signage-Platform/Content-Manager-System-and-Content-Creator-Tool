import { useEffect, useState } from "react";
import { PageTitle, GroupBar, MonitorRow } from "../../components";
import { ReactComponent as MonitorsIcon } from "../../static/monitors.svg"
import monitorService from "../../services/monitorService"
import monitorsGroupService from "../../services/monitorsGroupService"
import { Link, useNavigate } from "react-router-dom";
import DataTable, { createTheme } from 'react-data-table-component';

function Monitors(){
    const [monitors, setMonitors] = useState([]);
    const [groupId, setGroupId] = useState(null);
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

    }, [groupId]);


    const customStyles = {
        headRow: {
            style: {
                minHeight: '40px',
                borderBottomWidth: '1px',
                borderBottomStyle: 'solid',
            },
        },
        rows: {
            style: {
                
                minHeight: '40px', // override the row height
            },
        },
        headCells: {
            style: {
                paddingLeft: '8px', // override the cell padding for head cells
                paddingRight: '8px',
            },
        },
        cells: {
            style: {
                paddingLeft: '8px', // override the cell padding for data cells
                paddingRight: '8px',
            },
        },
    };

    createTheme('solarized', {
        text: {
          primary: '#101604',
        },
        background: {
          default: '#fafdf7',
        },
        divider: {
          default: '#073642',
        },
      });


    const columns = [
        {
            name: 'Name',
            selector: row => row.name,
            sortable: true,
        },
        {
            name: 'Group',
            selector: row => !row.group.madeForMonitor ? row.group.name:"-----",
            sortable: true,
        },
        {
            name: 'Warnings',
            selector: row => row.warnings,
            sortable: true,
        },
        {
            name: 'Status',
            selector: row => <div className=" w-[42px] bg-primary h-[20px] rounded-xl border-black border-2"></div>,
            sortable: true
        }   
    ];


    // keeping this code here as a reference
    /*
    <div id="content" className="w-full pr-3 pl-3 flex flex-col">
    <div id="Title_Row" className="text-2xl flex px-3 pt-3 justify-items-center text-center mt-1 mb-2">
        <span className="flex gap-2 items-center w-[40%]"><MonitorsIcon className="size-8"/>All Monitors</span>
        <span className="text-center">Group</span>
        <span className="ml-auto w-[10%]">Warnings</span>
        <span className="w-[10%]">Status</span>
        <span className="w-[10%] text-right">IP</span>
    </div>
    <div className="overflow-scroll">
        {monitors != [] && monitors.map((monitor,index)=>
            <Link to={`/monitor/${monitor.id}`} state={{ monitor:monitor}}>
                <MonitorRow monitor={monitor} index={index}/>
            </Link>
        )}
    </div>
</div>
    */
    

    return(
        <div className="flex flex-col h-full">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"monitors"} 
                            middleTitle={"dashboard"}
                            endTitle={"monitors"}/>
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