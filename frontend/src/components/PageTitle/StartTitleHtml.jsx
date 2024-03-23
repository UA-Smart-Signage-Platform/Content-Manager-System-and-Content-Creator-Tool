import { MdAccountCircle } from "react-icons/md";


function StartTitleHtml( {page} ){
    if (page === "default")
        return (
        <div className="flex">
            <MdAccountCircle className="h-6 w-6"/>
            <span className="ml-2">
                Welcome Nuno
            </span>
        </div>
        )
    if (page === "media")
        return(
            <div className="flex ml-2 font-medium">
                <span className="text-2xl">Media Images</span>
            </div>
        )
    if (page === "...")
        return(
            <div></div>
        )
}

export default StartTitleHtml