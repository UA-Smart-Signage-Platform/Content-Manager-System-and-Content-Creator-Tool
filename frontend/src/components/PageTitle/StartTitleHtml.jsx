import { MdAccountCircle, MdInfoOutline, MdMonitor, MdArrowDropUp } from "react-icons/md";
import { motion } from "framer-motion";
import { useState } from "react";

function StartTitleHtml( {page} ){
    const [displayInfo, setDisplayInfo] = useState(false);

    if (page === "default")
        return (
        <div className="flex">
            <MdAccountCircle className="h-6 w-6"/>
            <span className="ml-2">
                Welcome Nuno
            </span>
        </div>
        )
    if (page === "monitors")
        return(
                <span className="font-bold text-3xl">Monitors</span>
        )
    if (page === "media")
        return(
            <span className="font-bold text-3xl">Media</span>
        )
    if (page === "schedule")
        return(
            <div className="font-bold text-3xl flex flex-row place-content-end items-end">
                Schedule
                <motion.span
                    whileHover={{ scale: 1.2 }}
                    onMouseEnter={() => setDisplayInfo(true)}
                    onMouseLeave={() => setDisplayInfo(false)}
                    className="ml-2 relative"
                >
                    <MdInfoOutline className="w-6 cursor-pointer" />
                    {displayInfo &&
                        <>
                            <motion.span
                                className="absolute translate-x-[-50%] min-w-32 max-w-32 bg-black text-white text-xs rounded py-1 px-3"
                            >
                                Tooltip center
                                
                            </motion.span>
                            <MdArrowDropUp className="absolute top-0"/>
                        </>

                    }
                </motion.span>
            </div>
        )
    if (page === "monitor")
        return(
            <span className="font-bold text-3xl flex flex-row place-content-end items-end">
                <MdMonitor className="mx-2 text-4xl"/>
                Monitor
            </span>
        )
}

export default StartTitleHtml