import { useEffect, useState } from "react";
import { PageTitle, MemorySvg } from "../../components";
import { MdCreate, MdArrowBack, MdMonitor,MdCheck } from "react-icons/md";
import { useLocation, useParams } from "react-router";
import monitorService from "../../services/monitorService";
import monitorsGroupService from "../../services/monitorsGroupService";
import { IoWarningOutline } from "react-icons/io5";
import axios from "axios";
import { Link } from "react-router-dom";
import { motion,AnimatePresence } from "framer-motion";


function Monitor(){
    const { state } = useLocation();
    const [monitor,setMonitor] = useState(state);
    const [name,setName] = useState("");
    const [groupId,setGroupId] = useState(-1);
    const [groups,setGroups] = useState([]);
    const { id } = useParams();
    const [update,setUpdate] = useState(false);
    const [warning,setWarning] = useState(false);

    useEffect(()=>{
        axios.all([monitorService.getMonitorById(id),monitorsGroupService.getGroupsNotMadeForMonitor()]).then(
            axios.spread((monitorRes,groupsRes)=>{
                setMonitor(monitorRes.data)
                setName(monitorRes.data.name)
                setGroupId(monitorRes.data.group.id)
                let groupsSetting = groupsRes.data
                if (groupsSetting.find((element)=>element.id == monitorRes.data.group.id) == undefined){
                    groupsSetting.push(monitorRes.data.group)
                }
                setGroups(groupsSetting)
            })
        )
    },[update])

    const handleUpdate = ()=>{
        let monitorsend = monitor
        monitorsend.name = name
        monitorsend.group = groups.find((element)=>element.id == groupId)
        monitorService.updateMonitor(id,monitorsend).then((response)=>{
            setMonitor(response.data)
        })
        setUpdate(false)
    }

    
    if(monitor !== null)
        return(
            <div className="h-full flex flex-col">
                <AnimatePresence>
                    {warning && <motion.div className=" bg-rose-500 text-white p-4 absolute rounded-lg flex items-center justify-center gap-3 left-[15%]"
                                            initial={{y:-200}}
                                            animate={{y:0}}
                                            exit={{y:-200}}
                    >
                        <IoWarningOutline className="size-6"/>Cant Edit Group Of Monitor In Use.
                    </motion.div>}
                </AnimatePresence>
                <div id="title" className="pt-4 h-[8%]">
                    <PageTitle startTitle={"monitor"} 
                                middleTitle={"dashboard"}
                                endTitle={"dashboard"}/>
                </div>
                <div id="monitor" className="h-[92%]">
                    <div className="h-[8%] w-full flex flex-row">
                        <Link to="/monitors" className="w-[50%]"><div className="w-[100%] flex text-2xl gap-2 pb-2 items-end"><MdArrowBack className=" size-8"/> Go Back</div></Link>
                        <div className="w-[50%] flex text-2xl gap-2 pb-2 pl-4 items-end"><MdMonitor className=" size-8"/>Preview</div>
                    </div>
                    <div className="h-[92%] w-full flex flex-row gap-5">
                        <div className="w-[50%] h-full bg-secondaryLight rounded-[30px] flex flex-col p-5 gap-5">
                            <div className=" text-center text-4xl flex flex-row items-center justify-center gap-4">
                                {update ? 
                                    <div className="rounded-lg bg-secondaryMedium px-2 flex flex-row items-center">
                                        <input className="bg-secondaryMedium rounded-lg" value={name} onChange={(e)=>setName(e.target.value)}></input>
                                        <MdCreate className="ml-auto text-2xl text-gray-500"/>
                                    </div>
                                    :
                                    <div className="">{monitor.name}</div>
                                }
                                {update ? 
                                    <MdCheck className="transition ml-auto cursor-pointer text-3xl hover:text-4xl hover:translate-y-[-5px] hover:translate-x-1" onClick={()=>handleUpdate()}/>
                                    :
                                    <MdCreate className="transition ml-auto cursor-pointer text-3xl hover:text-4xl hover:translate-y-[-5px] hover:translate-x-1" onClick={()=>setUpdate(true)}/>
                                }
                            </div>
                            <div className=" bg-secondaryMedium rounded-[20px] h-[30%] text-center flex flex-col p-4 pb-6">
                                <p className="text-3xl">Template</p>
                                <p className="m-auto text-xl">DUMMY VALUE</p>
                            </div>
                            <div className="h-[60%] flex-col flex gap-3 text-center text-xl">
                                <div className="flex w-full gap-3 h-[49%] justify-around">
                                    <div className=" bg-secondaryMedium rounded-[10px] rounded-tl-[30px] basis-1/3 flex flex-col items-center p-2 h-full"> 
                                        <div className="h-[20%]">Memory</div>
                                        <MemorySvg className=" h-[80%]" usado={"120"} max={"200"} full={0.8}/>
                                    </div>
                                    <div className=" bg-secondaryMedium rounded-[10px] basis-1/3 p-2"> 
                                        <div className="h-[20%]">
                                            <span>IP</span>
                                        </div>
                                        <div className="h-[80%] flex items-center w-full pb-[20%]">
                                            <span className="text-xl text-center w-full">{monitor.ip}</span>
                                        </div>
                                    </div>
                                    <div className=" bg-secondaryMedium rounded-[10px] rounded-tr-[30px] basis-1/3 p-2"> 
                                        <div className="h-[20%]">
                                            <span className="flex flex-row gap-2 justify-center items-center">Group</span>
                                        </div>
                                        <div className="h-[80%] flex items-center w-full pb-[20%]">
                                            {update?
                                                <select disabled={monitor.group.templateGroups.length !== 0} className="rounded-lg bg-secondaryLight p-2 w-[80%] mx-auto" onChange={e => setGroupId(e.target.value)}
                                                        onMouseOver={()=>{if(monitor.group.templateGroups.length !== 0)setWarning(true)}}
                                                        onMouseLeave={()=>setWarning(false)}
                                                >
                                                    {groups.map((group)=>
                                                    <option key={group.id} value={group.id} selected={groupId== group.id}>{!group.madeForMonitor ? group.name:"----"}</option>
                                                )}
                                                </select>
                                                :
                                                <span className="text-xl text-center w-full">{!monitor.group.madeForMonitor ? monitor.group.name:"----"}</span>
                                            }
                                        </div>
                                    </div>

                                </div>
                                <div className="flex w-full gap-3 h-[49%] justify-around">
                                    <div className=" bg-secondaryMedium rounded-[10px] rounded-bl-[30px] basis-1/3 p-2"> 
                                        <div className="h-[20%]">
                                            <span>Updated</span>
                                        </div>
                                        <div className="h-[80%] flex items-center w-full pb-[20%]">
                                            <span className="text-xl text-center w-full">DUMMY VALUE</span>
                                        </div>
                                    </div>
                                    <div className=" bg-secondaryMedium rounded-[10px] basis-1/3 p-2"> 
                                        <div className="h-[20%]">
                                            <span>Errors</span>
                                        </div>
                                        <div className="h-[80%] flex items-center w-full pb-[20%]">
                                            <span className="text-xl text-center w-full">DUMMY VALUE</span>
                                        </div>
                                    </div>
                                    <div className=" bg-secondaryMedium rounded-[10px] rounded-br-[30px] basis-1/3 p-2"> 
                                        <div className="h-[20%]">
                                            <span>Last Update</span>
                                        </div>
                                        <div className="h-[80%] flex items-center w-full pb-[20%]">
                                            <span className="text-xl text-center w-full">DUMMY VALUE</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="w-[50%] h-full">
                            <div className="h-[60%] bg-secondaryLight rounded-[30px] p-3 w-full">
                                <img src="https://w.wallhaven.cc/full/ex/wallhaven-exrqrr.jpg" className="h-full w-full  rounded-[20px]">

                                </img>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    else
        return(
            <div>Loading</div>
        )
}

export default Monitor;