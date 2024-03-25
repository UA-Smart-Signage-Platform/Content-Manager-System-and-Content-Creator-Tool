import { PageTitle, MediaRow } from '../../components'
import { MdOutlineInsertDriveFile, MdAdd, MdKeyboardArrowDown } from "react-icons/md";


function Media() {
    return (
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"media"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex flex-col h-[92%] mr-3 ml-3 ">
                <div id="mediaHeader" className="h-[6%] w-full text-xl flex">
                    <span className="flex mt-auto mb-auto rounded-md w-[3.5%] h-[50%] bg-secondaryLight mr-3 cursor-pointer">
                        <span className="h-full w-[60%]"><MdAdd className="h-full w-full"/></span>
                        <span id="headerDividerHr" className="h-full border-[1px] border-secondary ml-auto"/>
                        <span className="h-full"><MdKeyboardArrowDown className="h-full"/></span>
                    </span>
                    <span className="flex items-center w-[24%]">
                        <span className="h-full w-[7%]"><MdOutlineInsertDriveFile className="w-full h-full"/></span>
                        <span className="ml-4">Name</span>
                    </span>
                    <span className="flex items-center w-[15%]">Size</span>
                    <span className="flex items-center w-[15%] ml-auto">Date</span>
                    <span className="w-[35%]"></span>
                </div>
                <div id="dividerHr" className="border-[1px] border-secondary"/>
                <div className="h-[94%] flex flex-row">
                    <div id="mediaContent" className="flex flex-col w-[50%] ml-[4%] overflow-scroll max-h-[760px]">
                        <MediaRow type="folder"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                        <MediaRow type="image"/>
                    </div>
                    <div id="mediaDividerHr" className=" w-[1px] h-full border-[1px] border-secondary"/>
                    <div id="mediaImage" className="flex h-full w-[45%]">
                        <div id="mediaImagePreview" className="m-auto mt-[25%] text-2xl font-light">
                            <span>Select an image to preview it here</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )    
}
export default Media;