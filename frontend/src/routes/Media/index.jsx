import { PageTitle, MediaModal, FunctionModal } from '../../components';
import { MdAdd, MdOutlineFolder, MdOutlineInsertPhoto, MdLocalMovies, MdOutlineInsertDriveFile, MdOutlineFormatIndentIncrease, MdCalendarMonth, MdCheck } from "react-icons/md";
import { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import mediaService from '../../services/mediaService';
import { useLocation, useNavigate} from 'react-router';
import { AnimatePresence, motion } from 'framer-motion';
import { FiEdit, FiTrash2 } from "react-icons/fi";
import { useMutation, useQueries, useQueryClient } from '@tanstack/react-query';
import { customStyles } from './tableStyleConfig';
import Breadcrumbs from './breadCrumbs';
import { formatBytes, getFileIcon } from './utils';



function Media() {
    const queryClient = useQueryClient();

    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [currentFolder, setCurrentFolder] = useState(null);
    const [folder, setFolder] = useState({});
    const [file, setFile] = useState(null);
    const [fileType, setFileType] = useState(null);
    const [preview, setPreview] = useState(null);
    const [toDelete, setToDelete] = useState(null);
    const [toEdit, setToEdit] = useState(null);
    const [editFileName, setEditFileName] = useState(null);
    const [action, setAction] = useState("");

    const [showAddPortal, setShowAddPortal] = useState(false);
    const [deletePortal, setDeletePortal] = useState(false);
    
    const path = useLocation().pathname.replace("/media/home","/uploads").replace(/\/$/, '');
    const navigate = useNavigate();
    
    const [rootFilesQuery, fileByIdQuery] = useQueries({
        queries : [
            {
                queryKey: ['rootFiles', path, showAddPortal],
                queryFn: () => mediaService.getFilesAtRootLevel(),
                enabled: path === "/uploads",
            },
            {
                queryKey: ['fileById', path, showAddPortal, currentFolder],
                queryFn: () => mediaService.getFileOrDirectoryById(currentFolder),
                enabled: path !== "/uploads",
            }
        ]
    })

    useEffect(() => {
        if (rootFilesQuery.data) {
            setFolder({});
        } else if (fileByIdQuery.data) {
            setFolder(fileByIdQuery.data.data);
        }
    }, [rootFilesQuery.data, fileByIdQuery.data])


    const deleteFile = useMutation({
        mutationFn: () => mediaService.deleteFileOrFolder(toDelete),
        onSuccess: async () => {
            setDeletePortal(false);
            setPreview(null);
            setFile(null);

            await queryClient.invalidateQueries(
                {
                    predicate: (query) => 
                        query.queryKey.some((key) => 
                            ['rootFiles', 'fileById'].includes(key)
                        ),
                    refetchType: 'active',
                },
            );
        }
    });

    const editFile = useMutation({
        mutationFn: (id) => mediaService.editFileOrFolder(id, editFileName),
        onSuccess: async () => {
            setToEdit(null);
            setPreview(null);
            setFile(null);

            await queryClient.invalidateQueries(
                {
                    predicate: (query) => 
                        query.queryKey.some((key) => 
                            ['rootFiles', 'fileById'].includes(key)
                        ),
                    refetchType: 'active',
                },
            );
        }
    });
    
    const { mutate: deleteFileMutate } = deleteFile;
    const { mutate: editFileMutate } = editFile;


    const showFilesDataTable = () => {
        const inRoot = (path === "/uploads");
        const query = inRoot ? rootFilesQuery : fileByIdQuery;

        return (
            <DataTable className="p-3" 
            defaultSortFieldId="isFolder"
            theme='solarized'
            pointerOnHover
            highlightOnHover
            pagination
            columns={columns}
            progressPending={query.isLoading}
            data={(inRoot ? query.data?.data : query.data?.data.subDirectories) || []}
            onRowClicked={handleRowClick}
            customStyles={customStyles}/>
        )
    }
    

    const [stack, setStack] = useState([]);

    const pushToStack = (item) => {
      setStack(prevStack => [...prevStack, item]);
    };

    const columns = [
        {
            name: (
                <div className="flex flex-row">
                    <MdOutlineInsertDriveFile className="h-6 w-6 mr-2"/> Name
                </div>
            ),
            selector: row => columnNameData(row),
            width: '35%',
        },
        {
            id: 'isFolder',
            selector: row => row.type,
            omit: true,
        },
        {
            name: (                
                <div className="flex flex-row">
                    <MdOutlineFormatIndentIncrease className="h-6 w-6 mr-2"/> Size
                </div>
            ),
            selector: row => formatBytes(row.size, 0),
            sortable: true,
            width: '25%',
        },
        {
            name: (                
                <div className="flex flex-row pr-8">
                    <MdCalendarMonth className="h-6 w-6 mr-2"/> Date
                </div>
            ),
            selector: row => row.date,
            sortable: true,
            width: '25%',
            right: 'true',
        },
        {
            selector: row => columnEditData(row),
            sortable:false,
            width: '6%'
        },
        {
            selector: row => columnDeleteData(row),
            sortable:false,
            width: '6%'
        }
    ];

    const columnNameData = (row) => {
        return (
            <div>
                {toEdit === row.id ? 
                <div>
                    <input className="bg-primary rounded-sm w-full text-md px-2" value={editFileName} onChange={(e)=>setEditFileName(e.target.value)}/>
                </div> 
                : 
                <div data-tag="allowRowEvents" className="flex flex-row items-center">
                    {getFileIcon(row.type)}
                    <span data-tag="allowRowEvents" className="ml-2">{row.name}{row.extension === "" ? "" : "."}{row.extension}</span>
                </div>
                }
            </div>

        )
    }

    const columnEditData = (row) => {
        return (
            <div key={row.id + "_" + "edit"}>
                {toEdit === row.id ? 
                    <button onClick={()=>{editFileMutate(row.id)}} className=" border-[2px] border-black rounded-sm size-7 flex items-center justify-center">
                        <MdCheck className='size-5'/>
                    </button>
                    :
                    <button onClick={()=>{setToEdit(row.id); setEditFileName(row.name)}} className=" border-[2px] border-black rounded-sm size-7 flex items-center justify-center">
                        <FiEdit className='size-5'/>
                    </button>
                }
            </div>
        )
    }

    const columnDeleteData = (row) => {
        return (
            <div key={row.id + "_" + "delete"}>
                {toEdit !== row.id &&  
                <div>
                    <button onClick={()=>{setDeletePortal(true);setToDelete(row.id)}} className=" border-[2px] border-black rounded-sm size-7 flex items-center justify-center">
                        <FiTrash2 className='size-5'/>
                    </button>
                    <AnimatePresence>
                        {deletePortal && <FunctionModal
                            message={"Are you sure you want to delete this file/folder?"}
                            funcToExecute={()=>deleteFileMutate()}
                            cancelFunc={()=>{setDeletePortal(false)}}
                            confirmMessage={"Yes"}
                            />}
                    </AnimatePresence>
                </div>}
            </div>
        )
    }

    const handleRowClick = (row) => {
        if (row.type === "directory"){
            setPreview(null);
            setFile(null);
            pushToStack(row.id);
            setCurrentFolder(row.id);
            navigate(window.location.pathname.replace(/\/$/, '') + "/" + row.name);
        }
        else{
            const change = (row.name === file ? null : row.name);
            setFileType(row.type);
            setFile(change);
            if (change !== null){
                setPreview(import.meta.env.VITE_APP_API_URL + "/uploads/" + row.uuid + "." + row.extension);
            }
            else{
                setPreview(null);
            }
        }
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
                            middleTitle={"default"}
                            endTitle={"..."}/>
            </div>
            <div id="divider" className="flex flex-col h-[92%] mr-3 ml-3 ">
                <div id="mediaHeader" className="h-[6%] w-full text-xl flex">
                    <button onClick={() => setIsDropdownOpen(!isDropdownOpen)} className="flex mt-auto mb-auto rounded-md w-[3.5%] h-[50%] bg-secondaryLight mr-3 cursor-pointer">
                        <span className="h-full w-[60%]"><MdAdd className="h-full w-full"/></span>
                        <span className="h-full text-sm flex items-center pr-1">ADD</span>
                    </button>
                    {isDropdownOpen && (
                        <div className="fixed text-md mt-10 z-10 bg-slate-300 w-[10%] h-16 rounded-md">
                            <button onClick={() => {setShowAddPortal(true); setIsDropdownOpen(false); setAction("folder")}} 
                                    className="pl-1 pb-2 cursor-pointer">
                                    New Folder
                            </button>
                            <button onClick={() => {setShowAddPortal(true); setIsDropdownOpen(false); setAction("file")}} 
                                    className="pl-1 pb-2 cursor-pointer">
                                        New File
                            </button>
                        </div>
                    )}
                    <MediaModal
                            showPortal={showAddPortal} 
                            setShowPortal={setShowAddPortal} 
                            currentFolder={folder}
                            action={action}/>
                    <div className="flex items-center w-[24%] ml-6 flex-row">
                        <Breadcrumbs 
                            path={path} 
                            stack={stack} 
                            setCurrentFolder={setCurrentFolder} 
                            navigate={navigate} 
                            setPreview={setPreview} 
                            setFile={setFile} 
                        />
                    </div>
                </div>
                <div id="dividerHr" className="border-[1px] border-secondary"/>
                <div className="h-[94%] flex flex-row">
                    <div id="mediaContent" className="flex flex-col w-[50%] ml-[4%] overflow-scroll max-h-[760px]">
                        {showFilesDataTable()}
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