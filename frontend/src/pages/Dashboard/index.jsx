import { GroupBar, PageTitle } from '../../components'
import { MdAccountCircle, MdOutlineSearch } from "react-icons/md";


function Dashboard() {
    const startTitleHtml = [ 
        <div className="flex">
            <MdAccountCircle className="h-6 w-6"/>
            <span className="ml-2">
                Welcome Nuno
            </span>
        </div>
    ]

    const middleTitleHtml = []

    const endTitleHtml = [
        <div className="flex border-2 border-searchButton rounded-md drop-shadow-md">
            <input className="z-10 bg-secondaryLight rounded-l-sm pl-2 pr-2 text-textcolor"
                    placeholder="Search for monitor..." 
                    autocomplete="off"/>
            <button className="rounded-r-sm bg-searchButton">
                <MdOutlineSearch className="h-6 w-6 mr-1 ml-1"/>
            </button>
        </div>
    ]

    return (
        <div className="h-full flex flex-col">
            <div id="title" className="mt-4 h-[8%]">
                <PageTitle startTitle={startTitleHtml} 
                            middleTitle={middleTitleHtml}
                            endTitle={endTitleHtml}/>
            </div>
            <div id="divider" className="flex flex-row h-full">
                <div className="w-[30%]">
                    <GroupBar/>
                </div>
                <div id="content" className="flex w-full pr-3 pl-3">
                    Monitors
                </div>
            </div>
        </div>
    )
}

export default Dashboard;