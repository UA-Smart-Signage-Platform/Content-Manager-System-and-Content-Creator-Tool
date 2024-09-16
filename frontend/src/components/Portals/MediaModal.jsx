import { createPortal } from 'react-dom';
import { MdArrowBack } from "react-icons/md";
import { useState } from 'react';
import mediaService from '../../services/mediaService';
import PropTypes from 'prop-types';

function MediaModal({showPortal, setShowPortal, currentFolder, action }){

    const [file, setFile] = useState(null);
    const [folderName, setFolderName] = useState("");

    const submitFile = async (event) => {
        event.preventDefault();

        if (!file)
            return;

        let formData = new FormData();
        formData.append("file", file);

        // Add parent to object if not in root
        if (Object.keys(currentFolder).length !== 0){
            formData.append("parentId", currentFolder.id);
        }

        await mediaService.createFile(formData);
        setShowPortal(false);
    }


    const submitFolder = async () => {
        const folder = {
            name: folderName,
            type: "directory",
            parent: Object.keys(currentFolder).length === 0 ? null : currentFolder,
            size: 0,
        }

        await mediaService.createFolder(folder);
        setFolderName("")
        setShowPortal(false);
    }


    const showContent = () => {
        if (action === "folder") {
            return (
                <div className="h-[80%] p-[2%] text-lg flex flex-col">
                    <label htmlFor="folderName">Folder name:</label>
                    <input onChange={(event) => setFolderName(event.target.value.trim().replace("/",""))} value={folderName} className="rounded-md bg-slate-300 pl-2 pr-2 w-[55%]" type="text" placeholder="Name..." id="folderName" name="folderName" />
                    <button onClick={submitFolder} className="rounded-md bg-slate-300 mr-auto p-2 mt-10" type="submit">Create folder</button>
                </div>
            )
        }
        else {
            return (
                <div className="h-[80%] p-[2%] text-lg flex flex-col">
                    <form onSubmit={submitFile}>
                        <label htmlFor="file">Select File:</label>
                        <input autoFocus onChange={(e) => setFile(e.target.files[0])} type="file" id="file" name="file" />
                        <button className="rounded-md bg-slate-300 mr-auto p-2 mt-10" type="submit">Upload File</button>
                    </form>
                </div>
            )
        }
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
                                    {action === "folder" ? "Create new folder" : "Upload a new file"}
                                    {showContent()}
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

MediaModal.propTypes = {
    showPortal: PropTypes.bool.isRequired,
    setShowPortal: PropTypes.func.isRequired,
    currentFolder: PropTypes.object.isRequired,
}

export default MediaModal;