import { NavBar } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";

function Root(){
    const theme = useThemeStore((state) =>state.theme)

    return(
        <div className={`h-screen flex ${theme} text-textcolor bg-backgroundcolor`}>
            <NavBar/>
            <div id="body" className="w-full ml-[65px] p-4 pr-20 h-full">
                <Outlet/>
            </div>
        </div>
    )
}

export default Root;