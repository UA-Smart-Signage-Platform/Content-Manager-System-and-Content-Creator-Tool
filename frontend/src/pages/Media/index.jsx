import { PageTitle, MediaFileModal, MediaFolderModal } from '../../components';
import { MdAdd, MdOutlineFolder, MdOutlineInsertPhoto, MdLocalMovies, MdOutlineInsertDriveFile } from "react-icons/md";
import { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import mediaService from '../../services/mediaService';
import { useNavigate, useParams } from 'react-router';

function Media() {
    const [filesAndDirectories, setFilesAndDirectories] = useState([]);
    const [isDropdownOpen, setDropdownOpen] = useState(false);

    const [currentFolder, setCurrentFolder] = useState(null);
    const [folder, setFolder] = useState(null);

    const [updater, setUpdater] = useState(false);
    const [file, setFile] = useState(null);
    const [fileType, setFileType] = useState(null);
    const [preview, setPreview] = useState(null);

    const [showPortalFile, setShowPortalFile] = useState(false);
    const [showPortalFolder, setShowPortalFolder] = useState(false);

    const { path } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if(path === 'home'){
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
    }, [currentFolder, updater]);

    const columns = [
        {
            name: (
                <div className="flex flex-row">
                    <MdOutlineInsertDriveFile className="h-6 w-6 mr-2"/> Name
                </div>
            ),
            selector: (row) => {
                return(
                    <div data-tag="allowRowEvents" className="flex flex-row items-center">
                        {getFileIcon(row.type)}
                        <span data-tag="allowRowEvents" className="ml-2">{row.name}</span>
                    </div>
                )
            },
            width: '47%',
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
            width: '28%',
            right: 'true',
        }
    ];

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


    const getFileIcon = (type) => {
        switch (type) {
            case "directory":
                return <MdOutlineFolder data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
            case "image/png":
                return <MdOutlineInsertPhoto data-tag="allowRowEvents" className="h-7 w-7 mr-2"/>;
            case "video/mp4":
                return <MdLocalMovies data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
            default:
                break;
        }
    };

    const handleRowClick = (row) => {
        if (row.type === "directory"){
            setCurrentFolder(row.id);
            navigate(window.location.pathname + "&" + row.name);
        }
        else{
            const change = (row.name === file ? null : row.name);
            setFileType(row.type);
            setFile(change);
            if (change !== null){
                if (path === 'home' ){
                    setPreview("http://localhost:8080/uploads/" + row.name);
                }
                else{
                    // will be changed to row.path
                    const filePath = window.location.pathname.split("/")[2].replace("&", "/").replace("home/", "") + "/" + row.name;
                    setPreview("http://localhost:8080/uploads/" + filePath);
                }
            }
            else{
                setPreview(null);
            }
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

    const breadCrumbs = () => {
        return (
            <div className="flex flex-row">
                {path.split("&").map((folder, index, array) =>
                    <div key={index}>
                        <button onClick={() => { navigate("/media/" + folder) }}
                                onMouseOver={(e) => e.target.style.backgroundColor = 'blue'}
                                onMouseOut={(e) => e.target.style.backgroundColor = ''}
                                >
                                {folder}
                        </button>
                        {(array.length > index + 1) && <span className="pr-2 pl-2">&gt;</span>}
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
            fileType === "video/mp4" ? 
            <div>
                <span className="flex flex-row text-2xl items-center font-medium mb-3">
                    <MdLocalMovies data-tag="allowRowEvents" className="h-8 w-8 "/> 
                    Preview:
                </span>
                <video src={`${preview}`} controls muted/> 
            </div>
            : 
            <div>
                <span className="flex flex-row text-2xl items-center font-medium mb-3">
                    <MdOutlineInsertPhoto data-tag="allowRowEvents" className="h-8 w-8"/> 
                    Preview:
                </span>
                <img className=" max-h-[720px]" src={`${preview}`} alt={`${file}`}/>
            </div>
        )
    }

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