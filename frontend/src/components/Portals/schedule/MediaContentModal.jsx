import { createPortal } from 'react-dom';
import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import mediaService from '../../../services/mediaService';
import DataTable from 'react-data-table-component';
import { MdArrowBack } from "react-icons/md";
import { motion } from 'framer-motion';
import { columns, conditionalRowStyles, customStyles } from './mediaContentModalUtils';

function MediaContentModal( { setShowMediaContentPortal, localContent, setLocalContent, templateWidget, variableName } ) {
    const [filesAndDirectories, setFilesAndDirectories] = useState([]);

    const [currentFolder, setCurrentFolder] = useState(null);
    const [folderStack, setFolderStack] = useState([]);

    const [selectedRow, setSelectedRow] = useState(null);

    useEffect(() => {
        if(currentFolder === null){
            mediaService.getFilesAtRootLevel().then((response) => {
                setFilesAndDirectories(response.data);
            })
        }
        else{
            mediaService.getFileOrDirectoryById(currentFolder).then((response) => {
                const data = response.data.subDirectories;
                data.unshift({name: "...", type: "directory"});
                setFilesAndDirectories(data);
            })
        }
    }, [currentFolder]);

    const handleRowDoubleClick = (row) => {
        if (row.type === "directory"){
            if (row.name === "...") {
                navigateBack();
            }
            else{
                setFolderStack([...folderStack, currentFolder]);
                setCurrentFolder(row.id);
            }
            setSelectedRow(null);
        }
        else{
            handleLocalContent(row);
            setShowMediaContentPortal(false);
        }
    };

    const handleRowClick = (row) => {
        filesAndDirectories.map(item => {
            if (row.id !== item.id) {
              item.selected = false;
            }
            else{
                row.selected = true;
            }
        });
        setSelectedRow(row);
    }

    const navigateBack = () => {
        if (folderStack.length > 0) {
            setCurrentFolder(folderStack.pop());
            setFolderStack([...folderStack]); 
        }
        else {
            setCurrentFolder(null);
        }
    };

    const handleLocalContent = (row) => {
        setLocalContent({
            ...localContent, 
            [templateWidget.id]: {
                ...localContent[templateWidget.id],
                [variableName]: row.id
            }
        });
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
                        <div className="bg-[#fafdf7] text-[#101604] h-[65%] w-[50%] mx-auto rounded-xl p-[2%]">
                            <div className="h-[10%] flex text-3xl">
                                <button onClick={() => setShowMediaContentPortal(false)} className="flex flex-row">
                                    <MdArrowBack className="w-9 h-9 mr-2"/> 
                                    <span>Go back</span>
                                </button>
                            </div>
                            <div className="h-[90%] p-[4%]">
                                <div className="h-[5%] font-bold text-3xl">
                                    Choose file
                                </div>
                                <div className="h-[95%] p-[2%] text-lg flex flex-col">
                                    <div className="h-[80%] w-full">
                                        <DataTable className="p-3" 
                                            defaultSortFieldId="isFolder"
                                            noTableHead
                                            pointerOnHover
                                            highlightOnHover
                                            pagination
                                            columns={columns}
                                            data={filesAndDirectories}
                                            onRowClicked={handleRowClick}
                                            onRowDoubleClicked={handleRowDoubleClick}
                                            customStyles={customStyles}
                                            conditionalRowStyles={conditionalRowStyles}
                                            theme="solarized"
                                            paginationPerPage={6}
                                            paginationRowsPerPageOptions={[2, 3, 6]}/>
                                    </div>
                                    <div className="h-[20%] flex w-full items-center place-content-end">
                                        <div className="pr-6">
                                            {(selectedRow !== null && selectedRow.name !== "..."
                                                ?
                                                <button 
                                                    onClick={() => {handleLocalContent(selectedRow); setShowMediaContentPortal(false)}} 
                                                    className="bg-[#96d600] rounded-md p-2 pl-4 pr-4">
                                                        Submit
                                                </button>
                                                :
                                                <button disabled 
                                                    className="bg-[#96d600] opacity-50 cursor-not-allowed rounded-md p-2 pl-4 pr-4">
                                                        Submit
                                                </button>
                                            )}
                                        </div>
                                        {((selectedRow !== null && selectedRow.type === "directory")
                                            ?
                                            <button 
                                                onClick={() => handleRowDoubleClick(selectedRow)} 
                                                className="bg-[#E9E9E9] rounded-md p-2 pl-4 pr-4">
                                                    Open
                                                </button>
                                            :
                                            <button disabled 
                                                className="bg-[#E9E9E9] opacity-50 cursor-not-allowed rounded-md p-2 pl-4 pr-4">
                                                    Open
                                            </button>
                                        )}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </motion.div>
            </motion.div>,
        document.body
    );
}


export default MediaContentModal;