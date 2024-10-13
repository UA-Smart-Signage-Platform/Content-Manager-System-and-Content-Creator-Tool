import { MdCreate, MdCheck } from "react-icons/md";

export const showUpdatingMonitor = (updatingMonitor, name, setName, updateMonitor, setUpdatingMonitor, monitorByIdQuery) => {
    if (updatingMonitor){
        return (
            <>
                <div className="rounded-lg bg-secondaryMedium px-2 flex flex-row items-center">
                    <input className="bg-secondaryMedium rounded-lg" value={name} onChange={(e)=>setName(e.target.value)}></input>
                    <MdCreate className="ml-auto text-2xl text-gray-500"/>
                </div>
                <MdCheck className="transition ml-auto cursor-pointer text-3xl hover:text-4xl hover:translate-y-[-5px] hover:translate-x-1" onClick={()=>updateMonitor()}/>
            </>
        )
    }
    else {
        return (
            <>
                <div className="">{monitorByIdQuery.data.data.name}</div>
                <MdCreate className="transition ml-auto cursor-pointer text-3xl hover:text-4xl hover:translate-y-[-5px] hover:translate-x-1" onClick={()=>setUpdatingMonitor(true)}/>
            </>

        )
    }
}