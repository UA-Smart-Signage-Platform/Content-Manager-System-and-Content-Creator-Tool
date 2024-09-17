import { useEffect, useState } from "react";
import templateservice from "../../services/templateService";
import { PageTitle,FunctionModal } from "../../components";
import DataTable from "react-data-table-component";
import { motion,AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router";
import { FiTrash2 } from "react-icons/fi";
import { useQuery,useMutation } from "@tanstack/react-query";

const customStyles = {
    headRow: {
        style: {
            minHeight: '40px',
            borderBottomWidth: '1px',
            borderBottomStyle: 'solid',
        },
    },
    rows: {
        style: {
            
            minHeight: '40px', // override the row height
        },
    },
    headCells: {
        style: {
            paddingLeft: '3px', // override the cell padding for head cells
            paddingRight: '3px',
        },
    },
    cells: {
        style: {
            paddingLeft: '8px', // override the cell padding for data cells
            paddingRight: '8px',
        },
    },
};

const colors = [
    'bg-pink-200 border-pink-400',
    'bg-rose-200 border-rose-400',
    'bg-violet-200 border-violet-400',
    'bg-blue-200 border-blue-400',
    'bg-green-200 border-green-400',
    'bg-yellow-200 border-yellow-400',
    'bg-orange-200 border-orange-400',
    'bg-stone-200 border-stone-400'
];



function Templates(){
    const [templateDisplay,setTemplateDisplay] = useState(null);
    const [selectedColors,setSelectedColors] = useState([]);
    const [deletePortal,setDeletePortal] = useState(false);
    const navigate = useNavigate();

    const columns = [
        {
            name: 'Name',
            selector: row => row.name,
            sortable: true,
        },
        {
            name: "widget count",
            selector: row => row.widgets.length,
            sortable:true,
        },
        {
            selector: row => columnButtonDelete(row),
            sortable:false,
        }
    ];
    

    const columnButtonDelete = (row) => {
        return(
            <>
                <button onClick={()=>setDeletePortal(true)} 
                        className=" border border-black rounded-sm size-5 flex items-center justify-center disabled:text-gray-400 disabled:border-gray-400">
                    <FiTrash2/>
                </button>
                <AnimatePresence>
                {deletePortal && <FunctionModal
                    message={"Are you sure you want to delete this Template?"}
                    funcToExecute={()=>{deleteTemplate.mutate(row.id);
                                        setDeletePortal(false)
                    }}
                    cancelFunc={()=>{setDeletePortal(false)}}
                    confirmMessage={"Yes"}
                    />}
                </AnimatePresence>
            </>
        )
    }
    
    const deleteTemplate = useMutation({
        mutationFn: (id) => templateservice.deleteTemplate(id),
        onSuccess: () =>{
            queryTemplates.refetch();
        }
    })

    const queryTemplates = useQuery({
        queryKey:["templates"],
        queryFn: () =>  templateservice.getTemplates().then((response)=>{
            if(response.data.length !== 0){
                setTemplateDisplay(response.data[0])
            }
            return response.data;
        })
    })

    useEffect(()=>{
        if (!queryTemplates.isLoading && queryTemplates.data.length !== 0 && templateDisplay !== null){
            let widgetCount = templateDisplay.widgets.length;
            let arr = [];
            for (let i = 0; i < widgetCount; i++){
                arr.push(colors[Math.floor(Math.random() * colors.length)]);
            }
            setSelectedColors(arr)
        }

    },[templateDisplay,queryTemplates.data]);

    return(
        <div className="h-full w-full flex flex-col">
            <div className="h-[8%]">
                <PageTitle startTitle={"templates"} middleTitle={""} endTitle={""}/>
            </div>
            <div className="flex h-[92%]">
                <div className="h-full w-[30%] p-4 flex flex-col">
                    {queryTemplates.isLoading ? 
                    <div className="text-lg">Loding Templates</div>
                    :
                    <>
                        <div>
                            <button onClick={()=>navigate("/contentcreator/0")}
                                className=" bg-secondaryMedium p-1 rounded-md">
                                + Create Template
                            </button>
                        </div>
                        <DataTable
                            pointerOnHover
                            highlightOnHover
                            onRowClicked={(row)=> {navigate(`${row.id}`,{state:row})}}
                            onRowMouseEnter={(row) => setTemplateDisplay(row)}
                            columns={columns}
                            data={queryTemplates.data}
                            theme="solarized"
                            customStyles={customStyles}
                        />
                    </>
                    }
                </div>
                    <div className="h-full w-[70%] flex flex-col">
                        <div className="aspect-video relative border-4 border-gray-300 rounded-md my-auto">
                            {templateDisplay !== null && templateDisplay.widgets.map((templateWidget, index) => 
                            <motion.div key={templateWidget.id} className={`absolute`}
                                initial={{
                                    width: `${templateWidget.width}%`,
                                    height: `${templateWidget.height}%`,
                                    top: `${templateWidget.top}%`,
                                    left: `${templateWidget.left}%`,
                                    zIndex: templateWidget.zindex
                                }}
                                animate={{
                                    width: `${templateWidget.width}%`,
                                    height: `${templateWidget.height}%`,
                                    top: `${templateWidget.top}%`,
                                    left: `${templateWidget.left}%`,
                                    zIndex: templateWidget.zindex
                                }}
                                exit={{
                                    width: 0,
                                    height: 0,
                                    top: 0,
                                    left: 0,
                                    zIndex: templateWidget.zindex
                                }}
                                transition={{duration:1}}
                                > 
                                <div className={`h-full w-full absolute flex flex-col items-center place-content-center border-2 rounded-sm ${selectedColors[index]}`}>
                                    {templateWidget.widget.name}
                                </div>
                            </motion.div>
                            )}
                        </div>
                    </div>
            </div>
        </div>
    )

}

export default Templates;