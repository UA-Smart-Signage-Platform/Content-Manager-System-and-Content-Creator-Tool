import { PageTitle, MediaFileModal, MediaFolderModal, FunctionModal } from '../../components';
import { MdAdd, MdOutlineFolder, MdOutlineInsertPhoto, MdLocalMovies, MdOutlineInsertDriveFile } from "react-icons/md";
import { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import mediaService from '../../services/mediaService';
import { useLocation, useNavigate} from 'react-router';
import { AnimatePresence, motion } from 'framer-motion';
import { FiTrash2 } from "react-icons/fi";


const getFileIcon = (type) => {
    switch (type) {
        case "directory":
            return <MdOutlineFolder data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
        case "image/":
            return <MdOutlineInsertPhoto data-tag="allowRowEvents" className="h-7 w-7 mr-2"/>;
        case "video/mp4":
            return <MdLocalMovies data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
        default:
            break;
    }
};
// code taken from https://stackoverflow.com/questions/15900485/correct-way-to-convert-size-in-bytes-to-kb-mb-gb-in-javascript
const formatBytes = (bytes, decimals = 2) => {
    if (!+bytes) return '0 Bytes'

    const k = 1024
    const dm = decimals < 0 ? 0 : decimals
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']

    const i = Math.floor(Math.log(bytes) / Math.log(k))

    return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`
};

const headNameStyle = (row) => {
    return(
        <div data-tag="allowRowEvents" className="flex flex-row items-center">
            {getFileIcon(row.type)}
            <span data-tag="allowRowEvents" className="ml-2">{row.name}</span>
        </div>
    )
};

const customStyles = {
    head: {
        style: {
            fontSize: '20px',
        },
    },
    rows: {
        style: {
            fontSize: '16px',
        },
    },
};

function Media() {
    const [filesAndDirectories, setFilesAndDirectories] = useState([]);
    const [isDropdownOpen, setDropdownOpen] = useState(false);
    
    const [currentFolder, setCurrentFolder] = useState(null);
    const [folder, setFolder] = useState({});
    
    const [updater, setUpdater] = useState(false);
    const [file, setFile] = useState(null);
    const [fileType, setFileType] = useState(null);
    const [preview, setPreview] = useState(null);
    
    const [showPortalFile, setShowPortalFile] = useState(false);
    const [showPortalFolder, setShowPortalFolder] = useState(false);
    const [deletePortal,setDeletePortal] = useState(false);
    const [toDelete,setToDelete] = useState(null);
    
    const path = useLocation().pathname.replace("/media/home","/uploads").replace(/\/$/, '');
    const navigate = useNavigate();

    const columns = [
        {
            name: (
                <div className="flex flex-row">
                    <MdOutlineInsertDriveFile className="h-6 w-6 mr-2"/> Name
                </div>
            ),
            selector: row => headNameStyle(row),
            width: '35%',
        },
        {
            id: 'isFolder',
            selector: row => row.type,
            omit: true,
        },
        {
            name: 'Size',
            selector: row => formatBytes(row.size, 0),
            sortable: true,
            width: '25%',
        },
        {
            name: 'Date',
            selector: row => row.date,
            sortable: true,
            width: '25%',
            right: 'true',
        },
        {
            selector: row => <div key={row.id + "_" + "delete"}>
                                <button onClick={()=>{setDeletePortal(true);setToDelete(row.id)}} className=" border-[2px] border-black rounded-sm size-7 flex items-center justify-center">
                                    <FiTrash2 className='size-5'/>
                                </button>
                                <AnimatePresence>
                                    {deletePortal && <FunctionModal
                                        message={"Are you sure you want to delete this file/folder?"}
                                        funcToExecute={()=>deleteFile()}
                                        cancelFunc={()=>{setDeletePortal(false)}}
                                        confirmMessage={"Yes"}
                                        />}
                                </AnimatePresence>
                            </div>,
            sortable:false,
            width: '15%'
        }
    ];

    const fetchData = ()=>{
        setPreview(null)
        setFile(null)
        if(path === "/uploads"){
            mediaService.getFilesAtRootLevel().then((response)=>{
                setFilesAndDirectories(response.data);
                setFolder({});
            })
        }
        else{
            mediaService.getDirectoryOrFileByPath(path).then((response)=>{
                setFilesAndDirectories(response.data.subDirectories);
                setFolder(response.data);
            })
        }
    }

    const deleteFile = ()=>{
        setDeletePortal(false);
        mediaService.deleteFileOrFolder(toDelete).then(()=>{
            fetchData();
        })
    }

    useEffect(() => {
        if(path === "/media"){
            navigate("/media/home")
            return
        }
        fetchData()
    }, [currentFolder, updater, path, navigate]);

    const handleRowClick = (row) => {
        if (row.type === "directory"){
            setCurrentFolder(row.id);
            navigate(window.location.pathname.replace(/\/$/, '') + "/" + row.name);
        }
        else{
            const change = (row.name === file ? null : row.name);
            setFileType(row.type);
            setFile(change);
            if (change !== null){
                if (path === 'home' ){
                    setPreview(import.meta.env.VITE_APP_API_URL + "/uploads/" + row.name);
                }
                else{
                    setPreview(import.meta.env.VITE_APP_API_URL + path + "/" + row.name);
                }
            }
            else{
                setPreview(null);
            }
        }
    };

    const breadCrumbsNavigate = (index) =>{
        let newpath = "home/" + path.split("/").slice(2,index + 2).join("/");
        navigate(newpath)
    }

    const breadCrumbs = () => {
        return (
            <div className="flex flex-row">
                {path.split("/").slice(1).map((folder, index, array) =>
                    <div key={folder + "_breadcrumb"}>
                        <motion.button key={folder.id} className={(index+1 !== array.length ? `text-secondary hover:bg-secondaryMedium rounded-lg px-1` : `text-black`)} onClick={() => breadCrumbsNavigate(index)}
                            whileHover={{scale:1.1}}
                        >
                                {folder !== "uploads" ? folder : "home"}
                        </motion.button>
                        {(array.length > index + 1) && <span className="pr-2 pl-2 text-secondary">&gt;</span>}
                    </div>
                )}
            </div>
        )
    };

    const showText = () => {
        return (
            <div id="mediaTextPreview" className="m-auto mt-[25%] text-2xl font-light">
                <span>Select a file to preview it here</span>
            </div>
        )
    };

    const showFilePreview = () => {
        return (
            <div>
                <span className="flex flex-row text-2xl items-center font-medium mb-3">
                    {fileType === "video/mp4" ? 
                        <MdLocalMovies data-tag="allowRowEvents" className="h-8 w-8" /> : 
                        <MdOutlineInsertPhoto data-tag="allowRowEvents" className="h-8 w-8" />}
                    Preview:
                </span>
                {fileType === "video/mp4" ? 
                        <video src={`${preview}`} controls muted/>  : 
                        <img className=" max-h-[720px]" src={`${preview}`} alt={`${file}`}/>}
            </div>
        )
    }

    return (
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"media"} 
                            middleTitle={"dashboard"}
                            endTitle={"..."}/>
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
                    <MediaFileModal
                            showPortal={showPortalFile} 
                            setShowPortal={setShowPortalFile} 
                            currentFolder={folder}
                            updater={updater}
                            setUpdater={setUpdater}/>
                    <MediaFolderModal
                            showPortal={showPortalFolder} 
                            setShowPortal={setShowPortalFolder} 
                            currentFolder={folder}
                            updater={updater}
                            setUpdater={setUpdater}/>
                    <div className="flex items-center w-[24%] ml-6 flex-row">
                        {breadCrumbs()}
                    </div>
                </div>
                <div id="dividerHr" className="border-[1px] border-secondary"/>
                <div className="h-[94%] flex flex-row">
                    <div id="mediaContent" className="flex flex-col w-[50%] ml-[4%] overflow-scroll max-h-[760px]">
                        <DataTable className="p-3" 
                            defaultSortFieldId="isFolder"
                            theme='solarized'
                            pointerOnHover
                            highlightOnHover
                            pagination
                            columns={columns}
                            data={filesAndDirectories}
                            onRowClicked={handleRowClick}
                            customStyles={customStyles}/>
                    </div>
                    <div id="mediaDividerHr" className=" w-[1px] h-full border-[1px] border-secondary"/>
                    <div id="mediaImage" className="flex h-full w-[45%]">
                        <div id="mediaPreview" className="flex w-[40%] ml-[1%] mt-[1%] fixed justify-center items-center">
                            {file === null ? showText() : showFilePreview()}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )    
}
export default Media;