import { PageTitle,MemorySvg } from "../../components";
import { MdCreate } from "react-icons/md";

function Monitor(){
    return(
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"dashboard"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="monitor" className="h-[92%]">
                <div className="h-[8%] w-full">

                </div>
                <div className="h-[92%] w-full flex flex-row gap-5">
                    <div className="w-[50%] h-full bg-secondaryLight rounded-[30px] flex flex-col p-5 gap-5">
                        <span className=" text-center text-3xl flex flex-row items-center justify-center gap-4">Monitor Located at 4.2.10<MdCreate/></span>
                        <div className=" bg-[#d7dad6] rounded-[20px] h-[30%] text-center flex flex-col p-4 pb-6">
                            <p className="text-3xl">Template</p>
                            <p className="m-auto text-xl">template de 50 anos universidade</p>
                        </div>
                        <div className="h-[60%] flex-col flex gap-3 text-center text-xl">
                            <div className="flex w-full gap-3 h-[49%] justify-around">
                                <div className=" bg-[#d7dad6] rounded-[10px] rounded-tl-[30px] basis-1/3 flex flex-col items-center p-2 h-full"> 
                                    <div className="h-[10%]">Memory</div>
                                    <MemorySvg className=" h-[90%]" usado={"120"} max={"500"} full={0.5}/>
                                </div>
                                <div className=" bg-[#d7dad6] rounded-[10px] basis-1/3 p-2"> IP</div>
                                <div className=" bg-[#d7dad6] rounded-[10px] rounded-tr-[30px] basis-1/3 p-2"> 
                                    <span className="flex flex-row gap-2 justify-center items-center">Group<MdCreate/></span>
                                </div>

                            </div>
                            <div className="flex w-full gap-3 h-[49%] justify-around">
                                <div className=" bg-[#d7dad6] rounded-[10px] rounded-bl-[30px] basis-1/3 p-2"> Updated</div>
                                <div className=" bg-[#d7dad6] rounded-[10px] basis-1/3 p-2"> Errors</div>
                                <div className=" bg-[#d7dad6] rounded-[10px] rounded-br-[30px] basis-1/3 p-2"> Last Update</div>
                            </div>
                        </div>
                    </div>
                    <div className="w-[50%] h-full bg-blue-500">
                        <div className="h-[50%] bg-secondaryLight rounded-[30px] w-full">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Monitor;