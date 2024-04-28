import { useRef } from "react";
import { MovableDiv } from "../../components"

function Cct(){
    const constrainRef = useRef(null);

    return(
        <div className="flex h-full w-full">
            <div className="h-full w-[20%] bg-green-300">

            </div>
            <div className="h-full w-[80%] bg-rose-400 flex items-center place-content-center">
                <div ref={constrainRef} className="border-emerald-600 border-2 aspect-video w-[90%] relative">
                    <MovableDiv parentRef={constrainRef} color={"bg-blue-500"}/>
                    <MovableDiv parentRef={constrainRef} color={"bg-rose-500"}/>
                </div>
            </div>
        </div>
    )
}

export default Cct;