import { useEffect, useState } from "react";
import monitorsGroupService from "../../services/monitorsGroupService"

function GroupBar({id,changeId}) {
    const [dataLoaded, setDataLoaded] = useState(false);
    const [groups, setGroups] = useState({});
 
    useEffect(() => {
        monitorsGroupService.getGroups().then((groupsData) => {
            setGroups(groupsData.data);
            setDataLoaded(true);
        })
    }, []);

    return (
        <div className="h-full flex flex-row">
            <div className="mt-4 ml-3 flex-col w-full">
                <div className="flex flex-col">
                    <span className="ml-3 mb-1 font-medium text-2xl">Overview</span>
                    <div id="selected" group-id="0" onClick={()=> changeId(null)} className={"cursor-pointer rounded-[4px] mb-4 mr-4 " +( id == null ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}>
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <span className="text-2xl">All monitors</span>
                            <span className="text-sm">All monitors in a single place</span>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col mt-5 overflow-scroll">
                    <span className="mb-1 font-medium text-2xl">Groups</span>
                    { dataLoaded && groups.map((group, index) => (
                    <div id="" group-id={group.id} onClick={()=> changeId(group.id)} className={`cursor-pointer rounded-[4px] mb-4 mr-4 `+ (group.id == id ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}>
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <span className="text-2xl">{group.name}</span>
                            <span className="text-sm">{group.description}</span>
                        </div>
                    </div>
                    ))}
                </div>
            </div>
            <div id="divider" className="mt-1 mb-1 w-[1px] h-full border-[1px] border-secondary"/>
        </div>
    )
}

export default GroupBar;