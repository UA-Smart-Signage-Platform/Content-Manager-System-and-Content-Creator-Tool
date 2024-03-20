import { MdOutlineDashboard,MdOutlinePermMedia,MdCalendarToday,MdMonitor,MdOutlineBrandingWatermark } from "react-icons/md";
import  {ReactComponent as Logo}  from "../../static/logo.svg"
import { AnimatePresence, motion } from "framer-motion";
import { useState } from "react";
import { Link } from "react-router-dom";


function NavBar() {
    const [isShow,setIsShow] = useState(false);

    return(
        <> 
        <div className=" flex flex-col w-[65px] bg-black h-[100vh] gap-10 z-10 text-white"
             onMouseEnter={()=>{setIsShow(true);}}
             onMouseLeave={()=>{setIsShow(false)}}>
            <div className="w-full h-[16%]">
                <Logo className="w-full h-full pl-2 pr-1"></Logo>
            </div>
            <Link to={"dashboard"} className="w-full h-[5%]">
                <MdOutlineDashboard className=" h-full w-full px-3.5 text-white"/>
            </Link>
            <Link to={"media/home"} className="w-full h-[5%]">
                <MdOutlinePermMedia className=" h-full w-full px-3.5"/>
            </Link>
            <Link to={"schedule"} className="w-full h-[5%]">
                <MdCalendarToday className=" h-full w-full px-3.5"/>
            </Link>
            <Link to={"monitors"} className="w-full h-[5%]">
                <MdMonitor className=" h-full w-full px-3.5"/>
            </Link>
            <Link to={"contentcreator"} className="w-full h-[5%]">
                <MdOutlineBrandingWatermark className=" h-full w-full px-3.5"/>
            </Link>
        </div>
        <AnimatePresence>
            {isShow && <motion.div className=" flex flex-col w-[10%] bg-black h-[100vh] gap-10 z-0 text-white fixed left-[65px] font-Lexend text-2xl font-semibold"
                initial={{x:-200}}
                animate={{x:0}}
                exit={{x:-200}}
                transition={{ease:"easeInOut",duration:0.4}}
                onMouseEnter={()=>{setIsShow(true);}}
                onMouseLeave={()=>{setIsShow(false)}}
                >
                <div className="w-full h-[16%] flex flex-col place-content-center font-bold">
                    <h>UA SMART</h>
                    <h className="ml-8">SIGNAGE</h> 
                </div>
                <Link to={"dashboard"} className="w-full h-[5%] flex items-center">
                    Dashboard
                </Link>
                <Link to={"media/home"} className="w-full h-[5%] flex items-center">
                    Media
                </Link>
                <Link to={"schedule"} className="w-full h-[5%] flex items-center">
                    Schedule
                </Link>
                <Link to={"monitors"} className="w-full h-[5%] flex items-center">
                    Monitors
                </Link>
                <Link to={"contentcreator"} className="w-full h-[5%] flex items-center">
                    Content Creator Tool
                </Link>
            </motion.div>}
        </AnimatePresence>
        </>
    )
}

export default NavBar;