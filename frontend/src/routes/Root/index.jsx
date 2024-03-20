import { NavBar, PageTitle } from "../../components";
import { Outlet } from "react-router";

function Root(){

    return(
        <div className="flex">
            <NavBar/>
            <div id="body" className="h-[100vh] w-full">
                <div id="border" className="p-4 pr-20 h-[100vh]">
                    <div id="title" className="mt-6 h-[8%]">
                        <PageTitle/>
                    </div>
                    <div id="content" className="h-[100vh]">
                        <Outlet/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Root;