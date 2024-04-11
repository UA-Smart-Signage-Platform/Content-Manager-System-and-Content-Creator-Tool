import { createPortal } from 'react-dom';
import { MdArrowBack, MdMonitor, MdCheck } from "react-icons/md";
import DataTable, { createTheme } from 'react-data-table-component';

function Portals( { page, showPortal, setShowPortal } ) {
    /*

    <div className="h-[14%] flex flex-col">
                                        <div className="flex flex-row">
                                            <div className="mr-auto flex flex-row text-xl items-center">
                                                <MdMonitor className="w-7 h-7 mr-2"/>
                                                192.0.168.172
                                            </div>
                                            <div className="ml-auto">
                                                <button className="bg-[#97D700] size-8 mr-3 rounded-sm"><MdCheck/></button>
                                                <button className="bg-[#D12E2E] size-8 rounded-sm">X</button>
                                            </div>
                                        </div>
                                        <div id="dividerHr" className="border-[1px] border-secondary mt-auto flex-col"/>
                                    </div>

    */


    const columns = [
        {
            name: 'IP',
            selector: row => row.ip,
        },
        {
            name: 'accept',
            cell: (row) => <button className="bg-[#97D700] size-8 mr-3 rounded-sm"><MdCheck/></button>
        },
        {
            name: 'decline',
            cell: (row) => <button className="bg-[#D12E2E] size-8 rounded-sm">{row.id}</button>
        },
    ];
    
    const data = [
            {
            id: 1,
            ip: '22'
        },
        {
            id: 2,
            ip: '22',
        },
        {
            id: 3,
            ip: '22',
        },
        {
            id: 4,
            ip: '22',
        },
    ]
                                
                        

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
                                    data={data}
                                />
                                    
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