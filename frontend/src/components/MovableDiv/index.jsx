import { useState,useRef } from "react";
import { motion,useMotionValue } from "framer-motion";

function MovableDiv( {parentRef,color,widget,widgetList,setWidgetList} ) {
    const divRef = useRef(null);
    const width = useMotionValue((widgetList[widget].width)+"%");
    const height = useMotionValue((widgetList[widget].height)+"%");
    const top = useMotionValue((widgetList[widget].top)+"%");
    const left = useMotionValue((widgetList[widget].leftPosition)+"%");
    const [initial,setInitial] = useState({x:0,y:0});
    const [initialWidth,setInitialWidth] = useState(null);
    const [initialHeight,setInitialHeight] = useState(null);
    const [status,setStatus] = useState({})
    const [colorResize,setColorResize] = useState("bg-black") 

    const updatePositionValues = (size) => {
        if (initialHeight !== null && initialWidth !== null){
            let newLeft = initialWidth + (((size.x - initial.x) * 100)/parentRef.current.offsetWidth)
            let newTop = initialHeight + (((size.y - initial.y) * 100)/parentRef.current.offsetHeight)
            left.set(newLeft > 0 ? newLeft < (100-Number(width.get().replace("%",""))) ? newLeft.toString().concat("%") : (100-Number(width.get().replace("%",""))).toString().concat("%") : "0%")
            top.set(newTop > 0 ? newTop < (100-Number(height.get().replace("%",""))) ? newTop.toString().concat("%") : (100-Number(height.get().replace("%",""))).toString().concat("%") : "0%")
        }
    }

    const changeStartPos = (size) =>{
        setInitial(size);
        setInitialWidth(Number(left.get().replace("%","")))
        setInitialHeight(Number(top.get().replace("%","")))
    }

    const changeStartSize = (size) =>{
        setInitial(size);
        setInitialWidth(Number(width.get().replace("%","")))
        setInitialHeight(Number(height.get().replace("%","")))
        setColorResize("");
    }

    const changeSize = (size)=>{
        if (initialHeight !== null && initialWidth !== null){
            width.set((initialWidth + (((size.x - initial.x) * 100)/parentRef.current.offsetWidth)).toString().concat("%"))
            height.set((initialHeight + (((size.y - initial.y) * 100)/parentRef.current.offsetHeight)).toString().concat("%"))
        }
    }

    const resetActions = () =>{
        setInitialHeight(null);
        setInitialWidth(null);
        let stat = {
            height:height.get(),
            width:width.get(),
            top:top.get(),
            left:left.get()
           }
        setStatus(stat);
        setColorResize("bg-black")
        console.log(stat);
    }

    return(
        <motion.div style={{width,height,top,left}}
                    className={` ${color} h-14 flex flex-col items-center justify-center absolute border-2`}
        >
            <div className="absolute top-[50%] translate-y-[-50%]">
                {widgetList[widget].widget.name} {widgetList[widget].id}
            </div>
            <div className="h-full w-full relative">
                <motion.div ref={divRef} className="h-full w-full"
                            drag
                            dragSnapToOrigin={true}
                            onDragStart={(event, info) => changeStartPos({x:info.point.x, y:info.point.y})}
                            onDrag={(event, info) => updatePositionValues({x:info.point.x, y:info.point.y})}
                            dragTransition={{ bounceStiffness: 6000, bounceDamping: 100000 }}
                            onDragEnd={resetActions}
                            >
                </motion.div>
                <motion.div drag
                            className={`${colorResize} h-[6px] w-[6px] cursor-move place-self-end absolute bottom-[-3px] right-[-3px] z-10`}
                            dragSnapToOrigin={true}
                            onDragStart={(event, info) => changeStartSize({x:info.point.x, y:info.point.y})}
                            onDrag={(event, info) => changeSize({x:info.point.x, y:info.point.y})}
                            dragTransition={{ bounceStiffness: 600, bounceDamping: 100000 }}
                            onDragEnd={resetActions}
                            >
                </motion.div>
            </div>
        </motion.div>
    )
}

export default MovableDiv;