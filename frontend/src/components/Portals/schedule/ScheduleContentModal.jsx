import { createPortal } from 'react-dom';
import { useState } from 'react';
import { MdArrowBack } from "react-icons/md";
import { motion, AnimatePresence } from 'framer-motion';
import Select from 'react-select'
import MediaContentModal from './MediaContentModal';

function ScheduleContentModal( { setShowContentsPortal, templateWidget, selectedContent, setSelectedContent } ) {

    const [localContent, setLocalContent] = useState(selectedContent);
    const [showMediaContentPortal, setShowMediaContentPortal] = useState(false);
    const [mediaVariableName, setMediaVariableName] = useState(-1);

    const handleSelectedContent = (event) => {
        setLocalContent({
            ...localContent, 
            [templateWidget.id]: {
                ...localContent[templateWidget.id],
                [event.value]: event.label
            }
        });
    }

    const variablesDisplay = () => {
        return(
            <div className="flex flex-col w-[30%]">
                {templateWidget.widget.variables.map((variable) => 
                    <div className="flex flex-col pb-5" key={templateWidget.id + "_" + variable.name}>
                        <label className="text-2xl mb-2 ml-2">{variable.name}</label>
                        {variable.optionsList !== null ? 
                            <Select
                                styles={{ menuPortal: base => ({ ...base, zIndex: 9999 }) }}
                                menuPortalTarget={document.body} 
                                isSearchable="true"
                                onChange={(e) => handleSelectedContent(e)}
                                options={variable.optionsList.map(option => ({ value: variable.name, label: option }))}
                                placeholder={templateWidget.widget.name + "..."}
                                defaultValue={localContent[templateWidget.id] !== undefined &&
                                                { label: localContent[templateWidget.id][variable.name], value: 1 }}
                                getOptionValue={(option) => option.label}
                            />
                        :
                        <button onClick={() => {setShowMediaContentPortal(true); setMediaVariableName(variable.name)}} 
                            className="bg-gradient-to-r from-purple-500 to-indigo-600 text-white rounded-full p-3 w-[40%] shadow-lg hover:from-purple-600 hover:to-indigo-700 transition-all duration-300 ease-in-out transform hover:scale-105">
                            Choose files
                        </button>
                        }
                    </div>
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
                    <div className="bg-black h-screen w-screen opacity-75"/>
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
                            <div className="h-[80%] p-[3%]">
                                <h1 className="text-4xl font-medium mb-4 ml-3">Adding contents to {templateWidget.widget.name} widget</h1>
                                <div className="flex flex-row h-full w-full justify-between p-4 border-2 rounded-lg border-gray-300">
                                    {variablesDisplay()}
                                </div>
                            </div>
                            <div className="h-[10%] flex justify-end items-center mr-[5%]">
                                <button onClick={() => {setSelectedContent(localContent); setShowContentsPortal(false)}} 
                                    className="text-lg bg-gradient-to-r from-green-400 to-blue-500 text-white font-bold rounded-lg p-4 shadow-lg hover:from-green-500 hover:to-blue-600 transition-all duration-300 ease-in-out transform hover:scale-105">
                                    Confirm choices
                                </button>
                            </div>
                        </div>
                    </motion.div>
                    <AnimatePresence>
                        {showMediaContentPortal && <MediaContentModal 
                                            setShowMediaContentPortal={setShowMediaContentPortal} 
                                            localContent={localContent} 
                                            setLocalContent={setLocalContent}
                                            templateWidget={templateWidget}
                                            variableName={mediaVariableName}/>
                        }
                    </AnimatePresence>
            </motion.div>,
        document.body
    );
}


export default ScheduleContentModal;