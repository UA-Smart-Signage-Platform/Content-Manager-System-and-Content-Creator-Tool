import { useState } from "react";
import { MdSettings, MdCreate, MdAdd, MdCheck } from "react-icons/md";
import { IoMdTrash } from "react-icons/io";
import { IoWarningOutline } from "react-icons/io5";
import monitorsGroupService from "../../services/monitorsGroupService"
import {motion,AnimatePresence} from "framer-motion"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import PropTypes from 'prop-types';

function GroupBar( {id, changeId, page} ) {
    const queryClient = useQueryClient();
    
    const [updatePage, setUpdatePage] = useState(false);
    const [warning,setWarning] = useState(false);
    const [editMode, setEditMode] = useState(false);
    const [editGroup,setEditGroup] = useState({ id:-1,
                                                name:"",
                                                description:""});

                                
    const groupsQuery = useQuery({ 
        queryKey: ['groupsQuery'], 
        queryFn: () => monitorsGroupService.getNonDefaultGroups()
    });

    const deleteGroupMutate = useMutation({
        mutationFn: (id) => monitorsGroupService.deleteGroup(id),
        onSuccess: async () => {
            await queryClient.invalidateQueries(
                {
                    queryKey: ['groupsQuery']
                },
            );
        }
    });

    const createOrUpdateGroupMutate = useMutation({
        mutationFn: () => {
            if (editGroup.id < 0) {
                return monitorsGroupService.createGroup(editGroup);
            } 
            else {
                return monitorsGroupService.updateGroup(editGroup.id, editGroup);
            }
        },
        onSuccess: async () => {
            await queryClient.invalidateQueries(
                {
                    queryKey: ['groupsQuery']
                }
            );
            setEditGroup({ id: -1, name: "", description: "" });
        }
    });

    const { mutate: deleteGroup } = deleteGroupMutate;

    const { mutate: createOrUpdateGroup } = createOrUpdateGroupMutate;

    const changeToEditMode = () => {
        if(editMode){
            setEditGroup({id:-1,name:"",description:""})
            groupsQuery.data.data = groupsQuery.data.data.filter((element)=> element.id !== -1)
        }
        setEditMode(!editMode);
    }

    const setupCreateGroup = () =>{
        if(!groupsQuery.data?.data.some((element)=> element.id == -1)){
            groupsQuery.data.data.push({ id: -1, name: "", description: "" });
            setUpdatePage(!updatePage);
        }
    }
    
    const editGroupName = (name) =>{
        setEditGroup({
            id:editGroup.id,
            name: name,
            description: editGroup.description,
        })
    }

    const editGroupDescription = (description) =>{
        setEditGroup({
            id:editGroup.id,
            name: editGroup.name,
            description: description,
        })
    }

    const handleBlur = (e) => {
        const currentTarget = e.currentTarget;
  
        // Give browser time to focus the next element
        requestAnimationFrame(() => {
          // Check if the new focused element is a child of the original container
          if (!currentTarget.contains(document.activeElement)) {
            setEditGroup({id:-1,name:"",description:""})
            groupsQuery.data.data = groupsQuery.data.data.filter((element)=> element.id !== -1)
          }})
    }

    return (
        <div className="h-full flex flex-row">
            <AnimatePresence>
                {warning && <motion.div className=" bg-rose-500 text-white p-4 absolute rounded-lg flex items-center justify-center gap-3 left-[15%]"
                                        initial={{y:-200}}
                                        animate={{y:-70}}
                                        exit={{y:-200}}
                >
                    <IoWarningOutline className="size-6"/>Can't Delete Group In Use
                </motion.div>}
            </AnimatePresence>
            <div className="mt-4 ml-3 flex-col w-full">
                <div className="flex flex-col">
                    <div className="flex flex-row">
                        <div>
                            <span className=" mb-1 font-medium text-2xl">Overview</span>
                        </div>
                        {page != null &&
                            <button className="ml-auto cursor-pointer pr-4" onClick={changeToEditMode}>
                                <motion.div
                                    animate={{rotate:editMode?90:0,
                                              scale:editMode?1.2:1   
                                    }}
                                    transition={{duration:0.2}}
                                ><MdSettings className="h-6 w-6"/></motion.div>
                            </button>
                        }
                    </div>
                    <button id="selected" onClick={()=> {changeId(null)}} className={"cursor-pointer rounded-[4px] mb-4 mr-4 " +( id === null ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}>
                        <div className="flex flex-col mt-1 mb-1 ml-4 text-left">
                            <div className="flex flex-row">
                                <div>
                                    <span className="text-2xl">All monitors</span>
                                </div>
                            </div>
                            <span className="text-sm">All monitors in a single place</span>
                        </div>
                    </button>
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
                    {groupsQuery.data?.data.map((group, index) => (
                    <button key={group.id}
                        onClick={()=> {changeId(group.id)}}
                        className={`cursor-pointer w-[95%] rounded-[4px] mb-4 mr-4 text-left `+ (group.id === id ? `bg-selectedGroup`:`bg-secondaryLight text-textcolorNotSelected `)}
                        onBlur={handleBlur}
                    >
                        <div className="flex flex-col p-2 h-[65px] w-full justify-around">
                            <div className="flex flex-row w-full place-items-center">
                                <div>
                                    {editGroup.id !== group.id ? 
                                        <span className="text-2xl">{group.name}</span> :
                                        <input className=" bg-white rounded-md w-full px-2" value={editGroup.name} autoFocus onChange={(e)=>editGroupName(e.target.value)}/>
                                    }
                                </div>
                                {editMode &&
                                    <div className="ml-auto">
                                        {editGroup.id !== group.id ? 
                                        <div className="flex gap-1">
                                            <button onClick={()=>setEditGroup(group)}>
                                                <MdCreate className="h-5 size-5 cursor-pointer"/>
                                            </button>
                                            <button disabled={group.monitors.length !== 0} onClick={() => deleteGroup(group.id)}
                                                    onMouseOver={()=>{if(group.monitors.length !== 0)setWarning(true)}}
                                                    onFocus={()=>{if(group.monitors.length !== 0)setWarning(true)}}
                                                    onBlur={()=>setWarning(false)}
                                                    onMouseLeave={()=>setWarning(false)}>
                                                <IoMdTrash className="h-5 size-5 cursor-pointer"/>
                                            </button>
                                        </div> 
                                        :
                                        <button><MdCheck className="size-5" onClick={() => createOrUpdateGroup()}/></button>
                                    }
                                    </div>
                                }
                            </div>
                            {editGroup.id !== group.id ? 
                                        <span className="text-sm">{group.description}</span> 
                                        :
                                        <input className=" bg-white rounded-md w-full text-sm px-2" value={editGroup.description} onChange={(e)=>editGroupDescription(e.target.value)}/>
                                    }
                        </div>
                    </button>
                    ))}
                </div>
            </div>
            <div id="divider" className="mt-1 mb-1 w-[1px] h-full border-[1px] border-secondary"/>
        </div>
    )
}

GroupBar.propTypes = {
    id: PropTypes.number.isRequired,
    changeId: PropTypes.func.isRequired,
    page: PropTypes.string.isRequired,
}

export default GroupBar;