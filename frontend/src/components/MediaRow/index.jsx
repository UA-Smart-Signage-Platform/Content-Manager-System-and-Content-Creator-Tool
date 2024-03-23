import { MdOutlineInsertPhoto, MdOutlineFolder } from "react-icons/md";

function MediaRow( { type } ) {
    let icon;

    if (type === "image"){
        icon = <MdOutlineInsertPhoto className="w-full h-full"/>;
    }
    else if (type === "folder"){
        icon = <MdOutlineFolder className="w-full h-full"/>;
    }
    else {

    }
    
    return(
         <div id="row" className="flex flex-col mr-[2%] hover:bg-secondaryLight h-full cursor-pointer">
            <div id="rowInfo" className="flex flex-row h-full mt-[1%] mb-[1%] items-center">
                <span className="flex items-center w-[25%]">
                    <span className="h-full w-[13%] ml-1">{icon}</span>
                    <span className="ml-4">Image.png</span>
                </span>
                <span className="mr-[10%] ml-auto">
                    26MB
                </span>
                <span className="ml-auto mr-[1%]">
                    10/12/2024
                </span>
            </div>
            <div id="dividerHrInfo-1" className="border-[1px] border-secondary"/>
        </div>
    )
}

export default MediaRow;