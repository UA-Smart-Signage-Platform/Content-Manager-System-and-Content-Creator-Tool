import { MdAccountCircle, MdOutlineSearch } from "react-icons/md";

function PageTitle() {
    return (
        <div className="flex flex-col h-full">
            <div id="centerItems" className="items-end h-full w-full flex">
                <div id="startTitle" className="ml-4 w-[20%] flex mb-3">
                    <MdAccountCircle className="h-6 w-6"/>
                    <span className="ml-2">
                        Welcome Nuno
                    </span>
                </div>
                <div id="middleTitle" className="w-[60%]">

                </div>
                <div id="endTitle" className="flex ml-auto mb-3 mr-1">
                    <div className="flex border-2 border-neutral-700 rounded-md drop-shadow-md">
                        <input className="bg-slate-600 rounded-l-sm pl-2"
                                placeholder="Search for monitor..." 
                                autocomplete="off"/>
                        <button className="bg-orange-700 rounded-r-sm">
                            <MdOutlineSearch className="h-6 w-6 mr-1 ml-1"/>
                        </button>
                    </div>
                </div>
            </div>
            <div id="divider" className="border-[1px] h-[2px] flex-col"/>
        </div>
    )
}

export default PageTitle;