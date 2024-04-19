import { createPortal } from 'react-dom';
import { MdArrowBack } from "react-icons/md";
import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import mediaService from '../../services/mediaService';
import DataTable from 'react-data-table-component';

import { MdOutlineFolder, MdOutlineInsertPhoto, MdLocalMovies, MdOutlineInsertDriveFile } from "react-icons/md";

function ScheduleContentModal( { showContentsPortal, setShowContentsPortal } ) {
    const [filesAndDirectories, setFilesAndDirectories] = useState([]);

    const [currentFolder, setCurrentFolder] = useState(null);
    const [folder, setFolder] = useState(null);

    const [file, setFile] = useState(null);
    const [path, setPath] = useState("home");

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
    }, [currentFolder]);

    const columns = [
        {
            name: (
                <div className="flex flex-row">
                    <MdOutlineInsertDriveFile className="h-6 w-6 mr-2"/> Name
                </div>
            ),
            selector: row => headNameStyle(row),
        },
        {
            id: 'isFolder',
            selector: row => row.type,
            omit: true,
        },
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


    const headNameStyle = (row) => {
        return(
            <div data-tag="allowRowEvents" className="flex flex-row items-center">
                {getFileIcon(row.type)}
                <span data-tag="allowRowEvents" className="ml-2">{row.name}</span>
            </div>
        )
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
            setPath(null);
            setCurrentFolder(row.id);
        }
        else{
            const change = (row.name === file ? null : row.name);
            setFile(change);
            if (change !== null){
                if (path === 'home' ){
                    console.log("http://localhost:8080/uploads/" + row.name);
                }
                else{
                    // will be changed to row.path
                    const filePath = window.location.pathname.split("/")[2].replace("&", "/").replace("home/", "") + "/" + row.name;
                    console.log("http://localhost:8080/uploads/" + filePath);
                }
            }
        }
    };

    return (
    <>
        {showContentsPortal && createPortal(
            <div className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                    <div className="bg-black h-screen w-screen opacity-75"></div>
                    <div className="absolute text-gray-50 h-screen w-screen flex items-center">
                        <div className="bg-[#fafdf7] text-[#101604] h-[75%] w-[55%] mx-auto rounded-xl p-[2%]">
                            <div className="h-[10%] flex">
                                <button onClick={() => setShowContentsPortal(false)} className="flex flex-row">
                                    <MdArrowBack className="w-7 h-7 mr-2"/> 
                                    <span className="text-xl">Go back</span>
                                </button>
                            </div>
                            <div className="h-[90%] p-[4%]">
                                <div className="h-[20%] font-bold text-3xl">
                                    Choose file
                                </div>
                                <div className="h-[80%] p-[2%] text-lg flex flex-col">
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
                            </div>
                        </div>
                    </div>
            </div>,
        document.body
        )}
    </>
    )
}


ScheduleContentModal.propTypes = {
    showPortal: PropTypes.bool.isRequired,
    setShowPortal: PropTypes.func.isRequired
}

export default ScheduleContentModal;