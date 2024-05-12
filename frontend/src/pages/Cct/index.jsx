import { useEffect, useRef, useState } from "react";
import { MovableDiv, PageTitle, WidgetsModal } from "../../components"
import { useLocation, useNavigate, useParams } from "react-router";
import { IoMdArrowUp, IoMdArrowDown } from "react-icons/io";
import templateservice from "../../services/templateService";
import widgetService from "../../services/widgetService";
import { motion } from "framer-motion";
import { FiTrash2 } from "react-icons/fi";

const colors = [
    'bg-pink-200 border-pink-400',
    'bg-rose-200 border-rose-400',
    'bg-violet-200 border-violet-400',
    'bg-blue-200 border-blue-400',
    'bg-green-200 border-green-400',
    'bg-yellow-200 border-yellow-400',
    'bg-orange-200 border-orange-400',
    'bg-stone-200 border-stone-400',
    'bg-deep-purple-200 border-deep-purple-400'
];


function Cct(){
    const { state } = useLocation();
    const [template,setTemplate] = useState(state !== null ? state:null);
    const [widgetList,setWidgetList] = useState(state !== null ? state.templateWidgets:[])
    const [name,setName] = useState(state !== null ? state.name:"")
    const [ableSave,setAbleSave] = useState(false);
    const [widgetsToRemove,setWidgetsToRemove] = useState([]);
    const [widgets,setWidgets] = useState([]);
    const [portal,setPortal] = useState(false);
    const constrainRef = useRef(null);
    const navigate = useNavigate();
    const { id } = useParams();

    useEffect(()=>{
        widgetService.getWidgets().then((response)=>{
            setWidgets(response.data)
        })
        if (id.toString() !== "0"){
            templateservice.getTemplate(id).then((response)=>{
                setTemplate(response.data)
                setWidgetList(response.data.templateWidgets)
                setName(response.data.name)
            })
        }else{
            setTemplate({
                id:null,
                name:"newTemplate",
            })
            setName("newTemplate")
            setWidgetList([])
        }

    },[id])

    const saveTemplate = ()=>{
        let savingTemplate = template;
        savingTemplate.templateWidgets = widgetList.map((element)=>{
            if(element.id.toString().includes("new")){
                element.id = null
            }
            return element
        });
        savingTemplate.name = name;

        templateservice.saveTemplate(savingTemplate).then((response)=>{
            let arr = widgetsToRemove.map((id)=>{
                return templateservice.deleteTemplateWidget(id)
            })
            Promise.all(arr).then(
                navigate("/contentcreator")
            )
        })
    }

    const zIndexUp = (id) =>{
        let zindex = widgetList.find((element)=> element.id === id).zindex
        if (zindex === widgetList.length){
            return
        }
        let arr = widgetList.map((element)=>{
            if(zindex === element.zindex){
                element.zindex = zindex+1
            }
            else if(zindex+1 === element.zindex){
                element.zindex = zindex
            }
            return element
        })
        setWidgetList(arr);
        if(!ableSave){
            setAbleSave(true);
        }
    }

    const zIndexDown = (id) =>{
        let zindex = widgetList.find((element)=> element.id === id).zindex
        if (zindex === 1){
            return
        }
        let arr = widgetList.map((element)=>{
            if(zindex === element.zindex){
                element.zindex = zindex-1
            }
            else if(zindex-1 === element.zindex){
                element.zindex = zindex
            }
            return element
        })
        setWidgetList(arr);
        if(!ableSave){
            setAbleSave(true);
        }
    }

    const removeWidget = (id,zindex) =>{
        let arr = widgetList.map((element)=>{
            if (element.zindex > zindex){
                element.zindex = element.zindex - 1
            }
            return element
        })
        setWidgetList(arr.filter((element)=> element.id !== id))
        setWidgetsToRemove(widgetsToRemove.concat([id]))
        if(!ableSave){
            setAbleSave(true)
        }
    }
    
    if (template == null){
        return(
            <div>
                Loading
            </div>
        )
    }else{
        return(
            <div className="flex flex-col h-full w-full">
                <div className="h-[8%]">
                    <PageTitle startTitle={"templates"}/>
                </div>
                <div className="h-[92%] flex">
                    {portal && <WidgetsModal setShowPortal={setPortal} Widgets={widgets} WidgetsList={widgetList} setWidgetList={setWidgetList}/>}
                    <div className="h-full w-[20%]">
                        <div className="font-bold pt-3 px-2 flex justify-between">
                            <input className="text-xl border-b-2 border-black" value={name} onChange={(e)=>{setName(e.target.value);setAbleSave(true)}}/>
                            <button disabled={!ableSave}
                                    onClick={saveTemplate}
                                    className=" bg-secondaryMedium p-1 rounded-md font-normal disabled:text-secondary disabled:bg-secondaryMedium">
                                + Save
                            </button>
                        </div>
                        <div className="flex font-bold px-2 pt-3 justify-between">
                            Widgets
                            <button className=" bg-secondaryMedium p-1 rounded-md font-normal" onClick={()=>setPortal(true)}>
                                + Add Widget
                            </button>
                        </div>
                        <div className="flex flex-col gap-1 p-3 relative">
                            {[].concat(widgetList).sort((a,b)=>{return b.zindex - a.zindex}).map((widget,index)=>
                                <motion.div key={widget.id} className={" rounded-md px-3 py-1 border flex justify-between items-center absolute w-full h-7 " + colors[widget.zindex-1]}
                                            initial={{x:0,y:0}}
                                            animate={{x:0,y:index * 30 }}
                                >
                                    <div>{widget.widget.name} {widget.id}</div> 
                                    <div className="flex gap-2">
                                        <motion.button onClick={()=>zIndexDown(widget.id)} whileHover={{scale:1.2}}
                                                className=" border border-black rounded size-5 flex justify-center items-center"><IoMdArrowDown/></motion.button>
                                        <motion.button onClick={()=>zIndexUp(widget.id)} whileHover={{scale:1.2}}
                                                className=" border border-black rounded size-5 flex justify-center items-center"><IoMdArrowUp /></motion.button>
                                        <motion.button onClick={()=>removeWidget(widget.id,widget.zindex)} whileHover={{scale:1.2}}
                                                className=" border border-black rounded size-5 flex justify-center items-center"><FiTrash2 /></motion.button>
                                    </div>
                                </motion.div>
                            )}
                        </div>
                    </div>
                    <div className="h-full w-[80%] flex items-center place-content-center">
                        <div ref={constrainRef} className="border-emerald-600 border-2 aspect-video w-[95%] relative">
                            {widgetList.map((widget)=>
                                <MovableDiv key={widget.id} parentRef={constrainRef} color={colors[widget.zindex-1]} widget={widget} widgetList={widgetList} setWidgetList={setWidgetList} setSave={setAbleSave}/>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        )

    }
    
}

export default Cct;