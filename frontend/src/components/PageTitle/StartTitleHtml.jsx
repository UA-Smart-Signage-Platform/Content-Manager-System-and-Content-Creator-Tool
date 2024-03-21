import { MdAccountCircle } from "react-icons/md";


function StartTitleHtml( {page} ){
    if (page === "dashboard")
        return (
        <div className="flex">
            <MdAccountCircle className="h-6 w-6"/>
            <span className="ml-2">
                Welcome Nuno
            </span>
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

export default StartTitleHtml