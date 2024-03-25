import { GroupBar, PageTitle } from '../../components'

function Dashboard() {
    return (
        <div className="h-full flex flex-col">
            <div id="title" className="mt-4 h-[8%]">
                <PageTitle startTitle={"default"} 
                            middleTitle={"default"}
                            endTitle={"default"}/>
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