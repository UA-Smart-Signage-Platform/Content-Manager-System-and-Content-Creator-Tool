import { createPortal } from 'react-dom';
import { MdArrowBack, MdMonitor, MdCheck } from "react-icons/md";
import DataTable, { createTheme } from 'react-data-table-component';
import { useEffect, useState } from 'react';
import monitorService from '../../services/monitorService';
import mediaService from '../../services/mediaService';

function Portals( { page, showPortal, setShowPortal,currentFolder } ) {

    const [pendingMonitors,setPendingMonitor] = useState([]);
    const [updater,setUpdater] = useState(false);
    const [file, setFile] = useState(null);
    const [folderName, setFolderName] = useState(null);

    useEffect(()=>{
        monitorService.getPendingMonitors().then((response)=>{
            setPendingMonitor(response.data)
        })
    },[updater])

    const handleAccept = (id)=>{
        monitorService.acceptMonitor(id).then((response)=>{
            setUpdater(updater ? setUpdater(false):setUpdater(true));
        });
        setPendingMonitor(pendingMonitors.filter(a=> a.id !== id));
    }

    const columns = [
        {
            name: 'IP',
            selector: row => row.ip,
        },
        {
            name: 'accept',
            cell: (row) => <button className="bg-[#97D700] size-8 mr-3 rounded-sm flex items-center text-lg" onClick={()=>handleAccept(row.id)}><MdCheck className='mx-auto'/></button>
        },
        {
            name: 'decline',
            cell: (row) => <button className="bg-[#D12E2E] size-8 rounded-sm flex items-center text-lg"><span className='mx-auto'>X</span></button>
        },
    ];

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

    const submitFolder = async () => {
        const folder = {
            name: folderName,
            type: "directory",
            parent: Array.isArray(currentFolder) ? null : currentFolder,
            size: 0,
        }

        await mediaService.createFolder(folder);
        setShowPortal(false);
    }

    if (page === "monitors")
        return (
        <>
            {showPortal && createPortal(
                <div className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                    <div className="bg-black h-screen w-screen opacity-75"></div>
                    <div className="absolute text-gray-50 h-screen w-screen flex items-center">
                        <div className="bg-[#fafdf7] text-[#101604] h-[75%] w-[70%] mx-auto rounded-xl p-[2%]">
                            <div className="h-[5%] flex items-center">
                                <button onClick={() => setShowPortal(false)} className="flex flex-row">
                                    <MdArrowBack className="w-7 h-7 mr-2"/> 
                                    <span className="text-xl">Go back</span>
                                </button>
                            </div>
                            <div className="h-[90%] p-[4%]">
                                <div className="h-[20%] font-bold text-3xl">
                                    Pending Monitors
                                </div>
                                <div className="h-[80%] p-[2%] text-lg flex flex-col">
                                <DataTable className="p-3" 
                                    noTableHead
                                    columns={columns}
                                    data={pendingMonitors}
                                />
                                </div>
                            </div>
                        </div>
                    </div>
              </div>,
            document.body
            )}
        </>
        )

    if (page === "mediaFile")
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

    if (page === "mediaFolder")
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
                                    Create new folder
                                </div>
                                <div className="h-[80%] p-[2%] text-lg flex flex-col">
                                    <label htmlFor="folderName">Folder name:</label>
                                    <input onChange={(event) => setFolderName(event.target.value)} className="rounded-md bg-slate-300 pl-2 pr-2 w-[55%]" type="text" placeholder="Name..." id="folderName" name="folderName" />
                                    <button onClick={submitFolder} className="rounded-md bg-slate-300 mr-auto p-2 mt-10" type="submit">Create folder</button>
                                </div>
                            </div>
                        </div>
                    </div>
              </div>,
            document.body
            )}
        </>
    );
}

export default Portals;