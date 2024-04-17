import { useEffect, useState,useCallback } from "react";
import { MdSettings, MdCreate,MdAdd,MdCheck } from "react-icons/md";
import monitorsGroupService from "../../services/monitorsGroupService"
import {motion} from "framer-motion"

function GroupBar( {id, changeId, page} ) {
    const [groups, setGroups] = useState([]);
    const [editMode, setEditMode] = useState(false);
    const [editGroup,setEditGroup] = useState({id:-1,name:"",description:""});
 
    useEffect(() => {
        monitorsGroupService.getGroups().then((groupsData) => {
            setGroups(groupsData.data);
        })
    }, []);

    const changeToEditMode = () => {
        if(editMode){
            setEditGroup({id:-1,name:"",description:""})
            setGroups(groups.filter((element)=> element.id !== -1))
        }
        setEditMode(!editMode);
    }

    const setupCreateGroup = () =>{
        if(!groups.some((element)=> element.id == -1))
            setGroups([...groups,{id:-1,name:"",description:""}])
    }

    const changeEditGroupName = (name) =>{
        setEditGroup({
            id:editGroup.id,
            name: name,
            description: editGroup.description,
        })
    }
    const cahngeEditGroupDescription = (description) =>{
        setEditGroup({
            id:editGroup.id,
            name: editGroup.name,
            description: description,
        })
    }
    
    const handleUpdateCreate = () =>{
        if (editGroup.id < 0){
            monitorsGroupService.createGroup(editGroup).then((response) =>{
                monitorsGroupService.getGroups().then((groupsData) => {
                    setGroups(groupsData.data);
                })
            })
        }else{
            monitorsGroupService.updateGroup(editGroup.id,editGroup).then((response) =>{
                monitorsGroupService.getGroups().then((groupsData) => {
                    setGroups(groupsData.data);
                })
            })

        }
        setEditGroup({id:-1,name:"",description:""})
    }

    const handleBlur = (e) => {
        const currentTarget = e.currentTarget;
  
        // Give browser time to focus the next element
        requestAnimationFrame(() => {
          // Check if the new focused element is a child of the original container
          if (!currentTarget.contains(document.activeElement)) {
            setEditGroup({id:-1,name:"",description:""})
            setGroups(groups.filter((element)=> element.id !== -1))
          }})
        }

    return (
        <div className="h-full flex flex-row">
            <div className="mt-4 ml-3 flex-col w-full">
                <div className="flex flex-col">
                    <div className="flex flex-row">
                        <div>
                            <span className=" mb-1 font-medium text-2xl">Overview</span>
                        </div>
                        {page != null &&
                            <div className="ml-auto cursor-pointer pr-4" onClick={changeToEditMode}>
                                <motion.div
                                    animate={{rotate:editMode?90:0,
                                              scale:editMode?1.2:1   
                                    }}
                                    transition={{duration:0.2}}
                                ><MdSettings className="h-6 w-6"/></motion.div>
                            </div>
                        }
                    </div>
                    <div id="selected" group-id="0" onClick={()=> changeId(null)} className={"cursor-pointer rounded-[4px] mb-4 mr-4 " +( id === null ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}>
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <div className="flex flex-row">
                                <div>
                                    <span className="text-2xl">All monitors</span>
                                </div>
                            </div>
                            <span className="text-sm">All monitors in a single place</span>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col mt-5 overflow-scroll">
                    <div className="flex flex-row justify-between">
                        <span className="mb-1 font-medium text-2xl">Groups</span>
                        {editMode && 
                            <button className="flex mt-auto mb-auto rounded-md w-[15%] h-[50%] bg-secondaryLight mr-3 cursor-pointer place-items-center"
                                    onClick={setupCreateGroup}
                            >
                                <span className="h-full w-[50%]"><MdAdd className="h-full w-full"/></span>
                                <span className="h-full text-sm flex pr-1">ADD</span>
                            </button>
                        }
                    </div>
                    { groups != null && groups.map((group, index) => (
                    <div id="" group-id={group.id} 
                        onClick={()=> changeId(group.id)} 
                        className={`cursor-pointer w-[95%] rounded-[4px] mb-4 mr-4 `+ (group.id === id ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}
                        onBlur={handleBlur}
                    >
                        <div className="flex flex-col p-2 h-[65px] w-full justify-around">
                            <div className="flex flex-row w-full place-items-center">
                                <div>
                                    {editGroup.id !== group.id ? 
                                        <span className="text-2xl">{group.name}</span> :
                                        <input className=" bg-white rounded-md w-full px-2" value={editGroup.name} autoFocus onChange={(e)=>changeEditGroupName(e.target.value)}/>
                                    }
                                </div>
                                {editMode &&
                                    <div className="ml-auto">
                                        {editGroup.id !== group.id ? 
                                        <MdCreate className="h-5 w-5 cursor-pointer" onClick={()=>setEditGroup(group)}/> :
                                        <button><MdCheck className="size-5" onClick={() => handleUpdateCreate()}/></button>
                                    }
                                    </div>
                                }
                            </div>
                            {editGroup.id !== group.id ? 
                                        <span className="text-sm">{group.description}</span> :
                                        <input className=" bg-white rounded-md w-full text-sm px-2" value={editGroup.description} onChange={(e)=>cahngeEditGroupDescription(e.target.value)}/>
                                    }
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