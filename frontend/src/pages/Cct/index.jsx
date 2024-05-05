import { useRef, useState } from "react";
import { MovableDiv, PageTitle } from "../../components"
import { useLocation, useParams } from "react-router";

function Cct(){
    const { state } = useLocation();
    const [template,setTemplate] = useState(state);
    const constrainRef = useRef(null);
    const { id } = useParams();



    return(
        <div className="flex flex-col h-full w-full">
            <div className="h-[8%]">
                <PageTitle startTitle={"templates"}/>
            </div>
            <div className="h-[92%] flex">
                <div className="h-full w-[30%] bg-green-300">

                </div>
                <div className="h-full w-[70%] bg-rose-400 flex items-center place-content-center">
                    <div ref={constrainRef} className="border-emerald-600 border-2 aspect-video w-[95%] relative">
                        <MovableDiv parentRef={constrainRef} color={"bg-blue-500"}/>
                        <MovableDiv parentRef={constrainRef} color={"bg-rose-500"}/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Cct;