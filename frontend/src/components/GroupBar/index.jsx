import { useEffect, useState } from "react";
import { MdSettings, MdCreate } from "react-icons/md";
import monitorsGroupService from "../../services/monitorsGroupService"

function GroupBar( {id, changeId, page} ) {
    const [groups, setGroups] = useState(null);
    const [editMode, setEditMode] = useState(false);
 
    useEffect(() => {
        monitorsGroupService.getGroups().then((groupsData) => {
            setGroups(groupsData.data);
        })
    }, []);

    const changeToEditMode = () => {
        setEditMode(!editMode);
    }

    const changeInformation = () => {
        
    }


    return (
        <div className="h-full flex flex-row">
            <div className="mt-4 ml-3 flex-col w-full">
                <div className="flex flex-col">
                    <div className="flex flex-row">
                        <div>
                            <span className="ml-3 mb-1 font-medium text-2xl">Overview</span>
                        </div>
                        {page != null &&
                            <div className="ml-auto cursor-pointer" onClick={changeToEditMode}>
                                <MdSettings className="h-6 w-6 mr-5"/>
                            </div>
                        }
                    </div>
                    <div id="selected" group-id="0" onClick={()=> changeId(null)} className={"cursor-pointer rounded-[4px] mb-4 mr-4 " +( id === null ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}>
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <div className="flex flex-row">
                                <div>
                                    <span className="text-2xl">All monitors</span>
                                </div>
                                {editMode &&
                                <div className="ml-auto cursor-pointer" onClick={changeInformation}>
                                    <MdCreate className="h-5 w-5 mr-2"/>
                                </div>
                                }
                            </div>
                            <span className="text-sm">All monitors in a single place</span>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col mt-5 overflow-scroll">
                    <span className="mb-1 font-medium text-2xl">Groups</span>
                    { groups != null && groups.map((group, index) => (
                    <div id="" group-id={group.id} onClick={()=> changeId(group.id)} className={`cursor-pointer rounded-[4px] mb-4 mr-4 `+ (group.id === id ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}>
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <div className="flex flex-row">
                                <div>
                                    <span className="text-2xl">{group.name}</span>
                                </div>
                                {editMode &&
                                <div className="ml-auto cursor-pointer" onClick={changeInformation}>
                                    <MdCreate className="h-5 w-5 mr-2"/>
                                </div>
                                }
                            </div>
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