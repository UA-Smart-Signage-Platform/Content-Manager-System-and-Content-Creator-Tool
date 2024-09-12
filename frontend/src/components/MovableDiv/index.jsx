import React, { useState,useRef } from "react";
import { motion,useMotionValue } from "framer-motion";
import PropTypes from 'prop-types';

function MovableDiv( {parentRef,color,widget,widgetList,setWidgetList,setSave} ) {
    const divRef = useRef(null);
    const width = useMotionValue((widget.width)+"%");
    const height = useMotionValue((widget.height)+"%");
    const top = useMotionValue((widget.top)+"%");
    const left = useMotionValue((widget.left)+"%");
    const [initial,setInitial] = useState({x:0,y:0});
    const [initialWidth,setInitialWidth] = useState(null);
    const [initialHeight,setInitialHeight] = useState(null);
    const [colorResize,setColorResize] = useState("bg-black") 

    const percentagetoNumber = (percentage) =>{
        return Number(percentage.replace("%",""))
    }

    const numbertoPercentage = (number) =>{
        return number.toString().concat("%")
    }

    const updatePositionValues = (size) => {
        if (initialHeight !== null && initialWidth !== null){
            let newLeft = initialWidth + (((size.x - initial.x) * 100)/parentRef.current.offsetWidth)
            let newTop = initialHeight + (((size.y - initial.y) * 100)/parentRef.current.offsetHeight)
            
            widgetList.forEach((element)=>{
                if (element.id === widget.id){
                    return
                }
                if (Math.abs(element.left - newLeft) < 1){
                    newLeft = element.left;
                }
                if (Math.abs(element.top - newTop) < 1){
                    newTop = element.top;
                }
                if (Math.abs(element.left - (newLeft + percentagetoNumber(width.get()))) < 1){
                    newLeft = element.left - percentagetoNumber(width.get());
                }
                if (Math.abs(element.top - (newTop + percentagetoNumber(height.get()))) < 1){
                    newTop = element.top - percentagetoNumber(height.get());
                }
                if (Math.abs(element.top + element.height - (newTop)) < 1){
                    newTop = element.top + element.height;
                }
                if (Math.abs(element.left + element.width - (newLeft)) < 1){
                    newLeft = element.left + element.width;
                }

            })
            if(newLeft > 0){
                left.set(newLeft < (100-percentagetoNumber(width.get()))? numbertoPercentage(newLeft) : numbertoPercentage(100-percentagetoNumber(width.get())))
            }else{
                left.set("0%")
            }

            if(newTop > 0){
                top.set(newTop < (100-percentagetoNumber(height.get())) ? numbertoPercentage(newTop) : numbertoPercentage(100-percentagetoNumber(height.get())))
            }else{
                top.set("0%")
            }
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
            let newWidth = (initialWidth + (((size.x - initial.x) * 100)/parentRef.current.offsetWidth));
            let newHeigth = (initialHeight + (((size.y - initial.y) * 100)/parentRef.current.offsetHeight));
            
            if (newWidth > 100){
                newWidth = 100
            }
            if (newHeigth > 100){
                newHeigth = 100
            }
            width.set(numbertoPercentage(truncate4numbers(newWidth)))
            height.set(numbertoPercentage(truncate4numbers(newHeigth)))
        }
    }

    const truncate4numbers = (number)=>{
        let mult = number * 10000
        mult = Math.trunc(mult)
        return mult / 10000
    }

    const resetActions = () =>{
        setInitialHeight(null);
        setInitialWidth(null);
        let widgetobj = widget
        widgetobj.left = truncate4numbers(parseFloat(percentagetoNumber(left.get())));
        widgetobj.top = truncate4numbers(parseFloat(percentagetoNumber(top.get())));
        widgetobj.height = truncate4numbers(parseFloat(percentagetoNumber(height.get())));
        widgetobj.width = truncate4numbers(parseFloat(percentagetoNumber(width.get())));
        setWidgetList(widgetList.map((element,i)=>{
            if (element.id !== widgetobj.id){
                return element
            }else{
                return widgetobj
            }
        }))
        setColorResize("bg-black")
        setSave(true)
    }

    return(
        <motion.div key={widget.id} id={widget.id} style={{width,height,top,left,zIndex:widget.zindex}}
                    className={` ${color} h-14 flex flex-col items-center justify-center absolute border-2`}
        >
            <div className="absolute top-[50%] translate-y-[-50%]">
                {widget.widget.name} {widget.id}
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

MovableDiv.propTypes = {
    parentRef: PropTypes.object.isRequired,
    color: PropTypes.string.isRequired,
    widget: PropTypes.object.isRequired,
    widgetList:PropTypes.array.isRequired,
    setWidgetList:PropTypes.func.isRequired,
    setSave:PropTypes.func.isRequired,
}

export default MovableDiv;