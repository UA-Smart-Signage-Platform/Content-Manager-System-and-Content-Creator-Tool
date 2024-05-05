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
                            <motion.div
                                className="absolute left-[50%] translate-x-[-50%] min-w-64 max-w-64 bg-black text-white text-xs rounded py-1 px-3"
                            >
                                <div className="flex flex-col">
                                    To display contents:{/* */}
                                    <span>- Choose desired group</span>{/* */}
                                    <span>- Click add rule</span>{/* */}
                                    <span>- Choose template and options</span>{/* */}
                                    <span>- Select content</span>{/* */}
                                    <span>- Submit rule</span>{/* */}
                                    All done!
                                    for a more detailed walkthrough check out this [video]
                                </div>
                            </motion.div>
                            <MdArrowDropUp className="absolute top-[45%] left-[50%] translate-x-[-50%]"/>
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
    if (page === "templates")
        return(
            <span className="font-bold text-3xl flex flex-row place-content-end items-end">
                
                Templates
            </span>
        )
}

export default StartTitleHtml