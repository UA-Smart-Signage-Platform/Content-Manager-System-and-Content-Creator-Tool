import { createPortal } from 'react-dom';
import { MdArrowBack, MdMonitor, MdCheck } from "react-icons/md";
import { useEffect, useState } from 'react';
import monitorService from '../../services/monitorService';
import mediaService from '../../services/mediaService';

function MediaFileModal({showPortal, setShowPortal,currentFolder}){

    const [file, setFile] = useState(null);

    const submitFile = async (event) => {
        event.preventDefault();

        if (!file)
            return;

        let formData = new FormData();
        formData.append("file", file);

        if (!Array.isArray(currentFolder)){
            formData.append("parentId", currentFolder.id);
        }
        
        await mediaService.createFile(formData);
        setShowPortal(false);
    }

    return (
        <>
            {showPortal && createPortal(
                <div className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                    <div className="bg-black h-screen w-screen opacity-75"></div>
                    <div className="absolute text-gray-50 h-screen w-screen flex items-center">
                        <div className="bg-[#fafdf7] text-[#101604] h-[45%] w-[30%] mx-auto rounded-xl p-[2%]">
                            <div className="h-[10%] flex">
                                <button onClick={() => setShowPortal(false)} className="flex flex-row">
                                    <MdArrowBack className="w-7 h-7 mr-2"/> 
                                    <span className="text-xl">Go back</span>
                                </button>
                            </div>
                            <div className="h-[90%] p-[4%]">
                                <div className="h-[20%] font-bold text-3xl">
                                    Upload a new file
                                </div>
                                <div className="h-[80%] p-[2%] text-lg flex flex-col">
                                    <form onSubmit={submitFile}>
                                        <label htmlFor="file">Select File:</label>
                                        <input onChange={(e) => setFile(e.target.files[0])} type="file" id="file" name="file" />
                                        <button className="rounded-md bg-slate-300 mr-auto p-2 mt-10" type="submit">Upload File</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
              </div>,
            document.body
            )}
        </>
    )
}

export default MediaFileModal;