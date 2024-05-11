import { createPortal } from 'react-dom';
import { MdArrowBack } from "react-icons/md";
import { useState } from 'react';
import PropTypes from 'prop-types';
import DataTable from 'react-data-table-component';

const columns =[
    {
        selector: row => row.name
    }
]
const customStyles = {
    head: {
        style: {
            fontSize: '20px',
        },
    },
    rows: {
        style: {
            fontSize: '13px',
        },
    }
};

function WidgetsModal( { Widgets, WidgetsList, setWidgetList,setShowPortal,setAbleSave } ) {

    const addWidget = (widget)=>{
        setWidgetList(WidgetsList.concat([{
            widget:widget,
            height:10,
            width:10,
            top:0,
            leftPosition:0,
            zindex:WidgetsList.length +1,
            id:"new" + (WidgetsList.length +1),
        }]))
        setShowPortal(false)
    }

    
    return (
    <>
        {createPortal(
            <div className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                <div className="bg-black h-screen w-screen opacity-75"></div>
                <div className="absolute text-gray-50 h-screen w-screen flex items-center">
                    <div className="bg-[#fafdf7] text-[#101604] h-[70%] w-[50%] mx-auto rounded-xl p-[2%]">
                        <div className="h-[10%] flex">
                            <button onClick={() => setShowPortal(false)} className="flex flex-row">
                                <MdArrowBack className="w-7 h-7 mr-2"/> 
                                <span className="text-xl">Go back</span>
                            </button>
                        </div>
                        <div className="h-[90%] p-[4%]">
                            <DataTable
                                data={Widgets}
                                columns={columns}
                                customStyles={customStyles}
                                pagination
                                onRowClicked={(row)=>addWidget(row)}
                                theme="solarized"
                            />
                        </div>
                    </div>
                </div>
            </div>,
        document.body
        )}
    </>
);
}

WidgetsModal.propTypes = {
    Widgets: PropTypes.array.isRequired,
    setWidgetList: PropTypes.func.isRequired,
    setShowPortal: PropTypes.func.isRequired,
    setAbleSave:PropTypes.func.isRequired,
}

export default WidgetsModal;