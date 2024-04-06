import { PageTitle, MemorySvg } from "../../components";
import { MdCreate, MdArrowBack, MdMonitor } from "react-icons/md";
import { useLocation } from "react-router";


function Monitor(){
    const { state } = useLocation();
    const monitor = state.monitor;
    console.log(monitor);

    return(
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"dashboard"} 
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
                        <span className=" text-center text-3xl flex flex-row items-center justify-center gap-4">{monitor.name}<MdCreate/></span>
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
                                        <span className="flex flex-row gap-2 justify-center items-center">Group<MdCreate/></span>
                                    </div>
                                    <div className="h-[80%] flex items-center w-full pb-[20%]">
                                        <span className="text-xl text-center w-full">{monitor.group.name}</span>
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
}

export default Monitor;