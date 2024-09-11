import { createPortal } from 'react-dom';
import { useState } from 'react';
import { MdArrowBack } from "react-icons/md";
import { motion } from 'framer-motion';

function ScheduleContentModal( { setShowContentsPortal, templateWidget } ) {

    const variablesDisplay = () => {
        return(
            <div className="flex flex-col w-[30%]">
                {templateWidget.widget.variables.map((variable) => 
                    <>
                        <label className="text-2xl mb-2 ml-2">{variable.name}</label>
                        {variable.optionsList !== null ? variable.map((option) =>
                            <select className="bg-gray-200 p-2 rounded mb-4">
                                <option>{option}</option>
                            </select>
                        )
                        :
                        <button className="bg-[#96d600] rounded-md p-2 pl-4 pr-4 w-[40%]">
                            Choose files
                        </button>
                        }
                    </>
                )}
            </div>
        )
    }

    return createPortal (
            <motion.div key="backgroundContents"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }} 
                className="fixed z-20 top-0 h-screen w-screen backdrop-blur-sm flex">
                    <div className="bg-black h-screen w-screen opacity-75"></div>
                    <motion.div key="contents"
                        initial={{ scale: 0.8 }}
                        animate={{ scale: 1 }}
                        exit={{ scale: 0.8 }}
                        transition={{ duration: 0.3, ease: "easeOut" }}
                        className="absolute text-gray-50 h-screen w-screen flex items-center">
                        <div className="bg-[#fafdf7] text-[#101604] h-[75%] w-[55%] mx-auto rounded-xl p-[2%]">
                            <div className="h-[10%] flex">
                                <button onClick={() => setShowContentsPortal(false)} className="flex flex-row">
                                    <MdArrowBack className="w-10 h-10 mr-2"/> 
                                    <span className="text-3xl mt-1">Go back</span>
                                </button>
                            </div>
                            <div className="h-[90%] p-[4%]">
                                <h1 className="text-4xl font-medium mb-4 ml-3">Adding contents to {templateWidget.widget.name} widget</h1>
                                <div className="flex flex-row h-full w-full justify-between p-4 border-2 rounded-lg border-gray-300">
                                    {variablesDisplay()}
                                </div>
                            </div>
                        </div>
                    </motion.div>
            </motion.div>,
        document.body
    );
}


export default ScheduleContentModal;