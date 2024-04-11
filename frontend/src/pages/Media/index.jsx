import { PageTitle, MediaRow } from '../../components';
import { MdOutlineInsertDriveFile, MdAdd, MdOutlineFolder, MdOutlineInsertPhoto } from "react-icons/md";
import mediaService from '../../services/mediaService';
import { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import Portals from '../../components/Portals'

function Media() {
    const [filesAndDirectories, setFilesAndDirectories] = useState([]);
    const [showPortalFile, setShowPortalFile] = useState(false);
    const [showPortalFolder, setShowPortalFolder] = useState(false);
    const [isDropdownOpen, setDropdownOpen] = useState(false);
    const [currentFolder, setCurrentFolder] = useState(null);
    const [previousFolder, setPreviousFolder] = useState(null);
    const [folder, setFolder] = useState(null);

    useEffect(() => {
        if(currentFolder === null){
            mediaService.getFilesAtRootLevel().then((response) => {
                setFilesAndDirectories(response.data);
                setFolder(response.data);
            })
        }
        else{
            mediaService.getFileOrDirectoryById(currentFolder).then((response) => {
                setFilesAndDirectories(response.data.subDirectories);
                setFolder(response.data);
            })
        }
    }, [currentFolder]);

    console.log(folder);

    const columns = [
        {
            name: 'Name',
            selector: row => row.name,
            cell: (row) => {
                return(
                    <div className="flex flex-row">
                        {getFileIcon(row.type)}
                        <span className="ml-2">{row.name}</span>
                    </div>
                )
            },
            sortable: true,
        },
        {
            name: 'Size',
            sortable: true,
        },
        {
            name: 'Date',
            sortable: true,
        }
    ];


    const getFileIcon = (type) => {
        switch (type) {
            case "directory":
                return <MdOutlineFolder/>;
            case "image/png":
                return <MdOutlineInsertPhoto/>;
            case "video":
                return <MdOutlineInsertPhoto/>;
            default:
                break;
        }
    }

    const handleRowClick = (row) => {
        if (row.type === 'directory') {
            setPreviousFolder(currentFolder);
            setCurrentFolder(row.id);
        } 
      };

    return (
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"media"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex flex-col h-[92%] mr-3 ml-3 ">
                <div id="mediaHeader" className="h-[6%] w-full text-xl flex">
                    <button onClick={() => setDropdownOpen(!isDropdownOpen)} className="flex mt-auto mb-auto rounded-md w-[3.5%] h-[50%] bg-secondaryLight mr-3 cursor-pointer">
                        <span className="h-full w-[60%]"><MdAdd className="h-full w-full"/></span>
                        <span className="h-full text-sm flex items-center pr-1">ADD</span>
                    </button>
                    {isDropdownOpen && (
                        <div className="fixed text-md mt-10 z-10 bg-slate-300 w-[10%] h-16 rounded-md">
                        <ul>
                            <li onClick={() => {setShowPortalFolder(true); setDropdownOpen(false)}} className="pl-1 pb-2 cursor-pointer">New Folder</li>
                            <li onClick={() => {setShowPortalFile(true); setDropdownOpen(false)}} className="pl-1 pb-2 cursor-pointer">New File</li>
                        </ul>
                        </div>
                    )}
                    <Portals page="mediaFile" 
                            showPortal={showPortalFile} 
                            setShowPortal={setShowPortalFile} 
                            currentFolder={folder}/>
                    <Portals page="mediaFolder" 
                            showPortal={showPortalFolder} 
                            setShowPortal={setShowPortalFolder} 
                            currentFolder={folder}/>
                    <span className="flex items-center w-[24%]">
                        <span className="h-full w-[7%]"><MdOutlineInsertDriveFile className="w-full h-full"/></span>
                        <span className="ml-4">Name</span>
                    </span>
                    <span className="flex items-center w-[15%]">Size</span>
                    <span className="flex items-center w-[15%] ml-auto">Date</span>
                    <span className="w-[35%]"></span>
                </div>
                <div id="dividerHr" className="border-[1px] border-secondary"/>
                <div className="h-[94%] flex flex-row">
                    <div id="mediaContent" className="flex flex-col w-[50%] ml-[4%] overflow-scroll max-h-[760px]">
                        <DataTable className="p-3" 
                            pointerOnHover
                            highlightOnHover
                            columns={columns}
                            data={filesAndDirectories}
                            onRowClicked={handleRowClick}
                        />
                    </div>
                    <div id="mediaDividerHr" className=" w-[1px] h-full border-[1px] border-secondary"/>
                    <div id="mediaImage" className="flex h-full w-[45%]">
                        <div id="mediaImagePreview" className="m-auto mt-[25%] text-2xl font-light">
                            <span>Select an image to preview it here</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )    
}
export default Media;