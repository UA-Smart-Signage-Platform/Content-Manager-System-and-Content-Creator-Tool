import { NavBar, PageTitle } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";

function Root(){
    const theme = useThemeStore((state) =>state.theme)

    return(
        <div className={`flex ${theme} text-textcolor bg-backgroundcolor`}>
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