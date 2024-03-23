
function GroupBar() {
    return (
        <div className="h-full flex flex-row">
            <div className="mt-4 ml-3 flex-col w-full">
                <div className="flex flex-col">
                    <span className="ml-3 mb-1 font-medium text-2xl">Overview</span>
                    <div className=" bg-selectedGroup rounded-[4px] mb-4 mr-4">
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <span className="text-2xl">All monitors</span>
                            <span className="text-sm">All monitors in a single place</span>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col mt-5 overflow-scroll">
                    <span className="mb-1 font-medium text-2xl">Groups</span>
                    <div className="bg-secondaryLight text-textcolorNotSelected rounded-[4px] mb-4 mr-4">
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <span className="text-2xl">DETI</span>
                            <span className="text-sm">Monitors from first floor, second and third</span>
                        </div>
                    </div>
                    <div className="bg-secondaryLight text-textcolorNotSelected rounded-[4px] mb-4 mr-4">
                        <div className="flex flex-col mt-1 mb-1 ml-4">
                            <span className="text-2xl">DETI Group 2</span>
                            <span className="text-sm">Monitors from amphitheater IV</span>
                        </div>
                    </div>
                </div>
            </div>
            <div id="divider" className="mt-1 mb-1 w-[1px] h-full border-[1px] border-secondary"/>
        </div>
    )
}

export default GroupBar;