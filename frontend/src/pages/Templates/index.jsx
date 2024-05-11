import { useEffect, useState } from "react";
import templateservice from "../../services/templateService";
import { PageTitle } from "../../components";
import DataTable from "react-data-table-component";
import { motion } from "framer-motion";
import { useNavigate } from "react-router";

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

const columns = [
    {
        name: 'Name',
        selector: row => row.name,
        sortable: true,
    },
    {
        name: "widget count",
        selector: row => row.templateWidgets.length,
        sortable:true,
    }
];

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
    const [templates,setTemplates] = useState([]);
    const [templateDisplay,setTemplateDisplay] = useState(null);
    const [selectedColors,setSelectedColors] = useState([]);
    const navigate = useNavigate();

    useEffect(()=>{
        templateservice.getTemplates().then((response)=>{
            setTemplates(response.data)
            setTemplateDisplay(response.data[0])
        })
    },[])

    useEffect(()=>{
        if (templates.length !== 0 && templateDisplay !== null){
            let widgetCount = templateDisplay.templateWidgets.length;
            let arr = [];
            for (let i = 0; i < widgetCount; i++){
                arr.push(colors[Math.floor(Math.random() * colors.length)]);
            }
            setSelectedColors(arr)
        }

    },[templateDisplay,templates]);

    return(
        <div className="h-full w-full flex flex-col">
            <div className="h-[8%]">
                <PageTitle startTitle={"templates"}/>
            </div>
            <div className="flex h-[92%]">
                <div className="h-full w-[30%] p-4 flex flex-col">
                    <div>
                        <button onClick={()=>navigate("/contentcreator/0")}
                             className=" bg-secondaryMedium p-1 rounded-md">
                            + Create Template
                        </button>
                    </div>
                    <DataTable
                        pointerOnHover
                        highlightOnHover
                        onRowClicked={(row)=> navigate(`${row.id}`,{state:row})}
                        onRowMouseEnter={(row) => setTemplateDisplay(row)}
                        columns={columns}
                        data={templates}
                        theme="solarized"
                        customStyles={customStyles}
                    />
                </div>
                    <div className="h-full w-[70%] flex flex-col">
                        <div className="aspect-video relative border-4 border-gray-300 rounded-md my-auto">
                            {templateDisplay !== null && templateDisplay.templateWidgets.map((templateWidget, index) => 
                            <motion.div key={index} className={`absolute`}
                                initial={{
                                    width: `${templateWidget.width}%`,
                                    height: `${templateWidget.height}%`,
                                    top: `${templateWidget.top}%`,
                                    left: `${templateWidget.leftPosition}%`,
                                    zIndex: templateWidget.zindex
                                }}
                                animate={{
                                    width: `${templateWidget.width}%`,
                                    height: `${templateWidget.height}%`,
                                    top: `${templateWidget.top}%`,
                                    left: `${templateWidget.leftPosition}%`,
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