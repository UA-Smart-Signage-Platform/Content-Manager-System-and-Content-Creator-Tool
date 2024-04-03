import { useEffect, useState } from "react";
import monitorsGroupService from "../../services/monitorsGroupService"

function GroupBar() {
    const [dataLoaded, setDataLoaded] = useState(false);
    const [groups, setGroups] = useState({});
 
    useEffect(() => {
        monitorsGroupService.getGroups().then((groupsData) => {
            setGroups(groupsData.data);
            setDataLoaded(true);
        })
    }, []);

    const changeGroup = (event) => {
        let currentElement = event.target;
        
        while(currentElement.getAttribute('group-id') === null){
            currentElement = currentElement.parentNode;
            if (!currentElement) break;
        }

        // If selecting a new group, change aspect
        if(!currentElement.classList.contains('bg-selectedGroup')){
            let previousElement = document.getElementById("selected");
            previousElement.id = "";
            previousElement.classList.toggle('bg-secondaryLight');
            previousElement.classList.toggle('text-textcolorNotSelected');
            previousElement.classList.toggle('bg-selectedGroup');

            currentElement.id = "selected";
            currentElement.classList.toggle('bg-secondaryLight');
            currentElement.classList.toggle('text-textcolorNotSelected');
            currentElement.classList.toggle('bg-selectedGroup');
        }
    }

    return (
        <div className="h-full flex flex-row">
            <div className="mt-4 ml-3 flex-col w-full">
                <div className="flex flex-col">
                    <span className="ml-3 mb-1 font-medium text-2xl">Overview</span>
                    <div id="selected" group-id="0" onClick={changeGroup} className="bg-selectedGroup rounded-[4px] mb-4 mr-4">
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <span className="text-2xl">All monitors</span>
                            <span className="text-sm">All monitors in a single place</span>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col mt-5 overflow-scroll">
                    <span className="mb-1 font-medium text-2xl">Groups</span>
                    { dataLoaded && groups.map((group, index) => (
                    <div id="" group-id={group.id} onClick={changeGroup} className="bg-secondaryLight text-textcolorNotSelected rounded-[4px] mb-4 mr-4">
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