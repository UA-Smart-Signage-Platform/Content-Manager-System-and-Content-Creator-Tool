import { MdOutlineSearch } from "react-icons/md";


function EndTitleHtml( {page} ){
    if (page === "dashboard")
        return (
            <div className="flex border-2 border-searchButton rounded-md drop-shadow-md">
                <input className="z-10 bg-secondaryLight rounded-l-sm pl-2 pr-2 text-textcolor"
                        placeholder="Search for monitor..." 
                        autocomplete="off"/>
                <button className="rounded-r-sm bg-searchButton">
                    <MdOutlineSearch className="h-6 w-6 mr-1 ml-1"/>
                </button>
            </div>
        )
    if (page === "")
        return(
            <div></div>
        )
    if (page === "...")
        return(
            <div></div>
        )
}
export default EndTitleHtml