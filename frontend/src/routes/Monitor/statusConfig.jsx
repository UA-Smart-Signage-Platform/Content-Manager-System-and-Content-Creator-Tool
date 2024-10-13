export const statusBlock = (online) => {
    return (
        <div className=" bg-secondaryMedium rounded-[10px] basis-1/3 p-2"> 
        <div className="h-[20%]">
            <span>Status</span>
        </div>
        <div className="h-[80%] flex items-center justify-center w-full">
            <div className="text-xl text-center w-full flex items-center justify-center h-full">
                {online ?
                                <div className=" w-[70%] bg-primary h-[50%] rounded-2xl border-black border-2 flex items-center justify-center font-bold">
                                    Online
                                </div> 
                                : 
                                <div className=" w-[70%] bg-red h-[50%] rounded-2xl border-black border-2 flex items-center justify-center font-bold">
                                    Offline
                                </div>
                }
            </div>
        </div>
    </div>
    )
}