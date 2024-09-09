export const groupBlock = (update, monitor, groups, groupId, setGroupId) => {
    return (
        <div className=" bg-secondaryMedium rounded-[10px] rounded-tr-[30px] basis-1/3 p-2"> 
            <div className="h-[20%]">
                <span className="flex flex-row gap-2 justify-center items-center">Group</span>
            </div>
            <div className="h-[80%] flex items-center w-full pb-[20%]">
                {update?
                    <select className="rounded-lg bg-secondaryLight p-2 w-[80%] mx-auto" onChange={e => setGroupId(e.target.value)}>
                        {groups.map((group)=>
                            <option key={group.id} value={group.id} selected={groupId== group.id}>{!group.madeForMonitor ? group.name:"----"}</option>
                        )}
                    </select>
                    :
                    <span className="text-xl text-center w-full">{!monitor.group.defaultGroup ? monitor.group.name:"----"}</span>
                }
            </div>
        </div>
    )
}