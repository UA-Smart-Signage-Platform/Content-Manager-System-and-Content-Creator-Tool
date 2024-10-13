import { useState } from "react";
import { PageTitle, GroupBar } from "../../components";
import monitorService from "../../services/monitorService"
import { useNavigate } from "react-router-dom";
import DataTable from 'react-data-table-component';
import { customStyles } from "./tableStyleConfig";
import { columns } from "./tableDataConfig";
import { useQuery } from "@tanstack/react-query";

function Monitors(){
    const [groupId, setGroupId] = useState(null);
    const [pageUpdate, setPageUpdate] = useState(false);
    const navigate = useNavigate();

    const monitorsQuery = useQuery({
        queryKey: ['monitorsQuery', groupId, pageUpdate],
        queryFn: () => {
            if(groupId === null) {
                return monitorService.getMonitors();
            }
            else {
                return monitorService.getMonitorsByGroup(groupId);
            }
        }
    });

    return(
        <div className="flex flex-col h-full">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"monitors"} 
                            middleTitle={"default"}
                            endTitle={"monitors"} updater={pageUpdate} setUpdater={setPageUpdate}/>
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
                    progressPending={monitorsQuery.isLoading}
                    data={monitorsQuery.data?.data}
                    theme="solarized"
                    customStyles={customStyles}/>
            </div>
        </div>
    )
}

export default Monitors;