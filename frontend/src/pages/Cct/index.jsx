import { useRef, useState } from "react";
import { MovableDiv, PageTitle } from "../../components"
import { useLocation, useParams } from "react-router";

const colors = [
    'bg-pink-200 border-pink-400',
    'bg-rose-200 border-rose-400',
    'bg-violet-200 border-violet-400',
    'bg-blue-200 border-blue-400',
    'bg-green-200 border-green-400',
    'bg-yellow-200 border-yellow-400',
    'bg-orange-200 border-orange-400',
    'bg-stone-200 border-stone-400'
];


function Cct(){
    const { state } = useLocation();
    const [template,setTemplate] = useState(state);
    const [widgetList,setWidgetList] = useState(state.templateWidgets)
    const [ableSave,setAbleSave] = useState(false);
    const constrainRef = useRef(null);
    const { id } = useParams();

    console.log(template)
    


    return(
        <div className="flex flex-col h-full w-full">
            <div className="h-[8%]">
                <PageTitle startTitle={"templates"}/>
            </div>
            <div className="h-[92%] flex">
                <div className="h-full w-[20%]">
                    <div className="font-bold pt-3 px-2 flex justify-between">
                        <div className="text-xl">
                            {template.name}
                        </div>
                        <button disabled={!ableSave} className=" bg-secondaryMedium p-1 rounded-md font-normal disabled:text-secondary disabled:bg-secondaryMedium">
                            + Save
                        </button>
                    </div>
                    <div className="flex font-bold px-2 pt-3 justify-between">
                        Widgets
                        <button className=" bg-secondaryMedium p-1 rounded-md font-normal">
                            + Add Widget
                        </button>
                    </div>
                    <div className="flex flex-col gap-1 p-3">
                        {widgetList.map((widget,index)=>
                            <div id={widget.id} className={" rounded-md px-3 py-1 border " + colors[index]}>
                                {widget.widget.name} {widget.id}
                            </div>
                        )}
                    </div>
                </div>
                <div className="h-full w-[80%] flex items-center place-content-center">
                    <div ref={constrainRef} className="border-emerald-600 border-2 aspect-video w-[95%] relative">
                        {widgetList.map((widget,index)=>
                            <MovableDiv parentRef={constrainRef} color={colors[index]} widget={index} widgetList={widgetList} setWidgetList={setWidgetList}/>
                        )}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Cct;