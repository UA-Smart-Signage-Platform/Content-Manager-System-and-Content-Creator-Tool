import { NavBar } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";

function Root(){
    const theme = useThemeStore((state) =>state.theme)

    return(
        <div className={`h-screen flex ${theme} text-textcolor bg-backgroundcolor`}>
            <NavBar/>
            <div id="body" className="h-[100vh] w-full">
                <div id="border" className="p-4 pr-20 h-full">
                    <Outlet/>
                </div>
            </div>
        </div>
    )
}

export default Root;