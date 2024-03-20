import { MdAccountCircle, MdOutlineSearch } from "react-icons/md";

function PageTitle({ startTitle, middleTitle, endTitle }) {
    return (
        <div className="flex flex-col h-full">
            <div id="centerItems" className="items-end h-full w-full flex">
                <div id="startTitle" className="ml-4 w-[20%] flex mb-3">
                    { startTitle }
                    <MdAccountCircle className="h-6 w-6"/>
                    <span className="ml-2">
                        Welcome Nuno
                    </span>
                </div>
                <div id="middleTitle" className="w-[60%]">
                    { middleTitle }
                </div>
                <div id="endTitle" className="flex ml-auto mb-3 mr-1">
                    { endTitle }
                    <div className="flex border-2 border-searchButton rounded-md drop-shadow-md">
                        <input className="z-10 bg-secondaryLight rounded-l-sm pl-2 pr-2 text-textcolor"
                                placeholder="Search for monitor..." 
                                autocomplete="off"/>
                        <button className="rounded-r-sm bg-searchButton">
                            <MdOutlineSearch className="h-6 w-6 mr-1 ml-1"/>
                        </button>
                    </div>
                </div>
            </div>
            <div id="divider" className="border-[1px] border-secondary flex-col"/>
        </div>
    )
}

export default PageTitle;