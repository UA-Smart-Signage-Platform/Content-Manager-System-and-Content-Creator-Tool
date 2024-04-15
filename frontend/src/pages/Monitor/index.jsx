import { useEffect, useState } from "react";
import { PageTitle, MemorySvg } from "../../components";
import { MdCreate, MdArrowBack, MdMonitor,MdCheck } from "react-icons/md";
import { useLocation, useParams } from "react-router";
import monitorService from "../../services/monitorService";
import monitorsGroupService from "../../services/monitorsGroupService";


function Monitor(){
    const { state } = useLocation();
    const [monitor,setMonitor] = useState(state);
    const [name,setName] = useState("");
    const [groupId,setGroupId] = useState();
    const [groups,setGroups] = useState([]);
    const { id } = useParams();
    const [update,setUpdate] = useState(false);

    useEffect(()=>{
        monitorService.getMonitorById(id).then((response)=>{
            setMonitor(response.data)
            setName(response.data.name)
            setGroupId(response.data.group.id)
        })
        monitorsGroupService.getGroups().then((response)=>{
            setGroups(response.data)
        })
    },[])

    const handleUpdate = ()=>{
        monitor.name = name
        monitor.group = groups.find((element)=>element.id == groupId)
        monitorService.updateMonitor(id,monitor).then((response)=>{
            setMonitor(response.data)
        })
        setUpdate(false)
    }

    
    if(monitor !== null)
        return(
            <div className="h-full flex flex-col">
                <div id="title" className="pt-4 h-[8%]">
                    <PageTitle startTitle={"monitor"} 
                                middleTitle={"dashboard"}
                                endTitle={"dashboard"}/>
                </div>
                <div id="monitor" className="h-[92%]">
                    <div className="h-[8%] w-full flex flex-row">
                        <div className="w-[50%] flex text-2xl gap-2 pb-2 items-end"><MdArrowBack className=" size-8"/> Go Back</div>
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
                                        <MemorySvg className=" h-[80%]" usado={"120"} max={`${monitor.size}`} full={0.5}/>
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
                                                <select className="rounded-lg bg-secondaryLight p-2 w-[80%] mx-auto" onChange={e => setGroupId(e.target.value)}>
                                                    {groups.map((group)=>
                                                    <option key={group.id} value={group.id} selected={groupId== group.id}>{group.name}</option>
                                                )}
                                                </select>
                                                :
                                                <span className="text-xl text-center w-full">{monitor.group.name}</span>
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