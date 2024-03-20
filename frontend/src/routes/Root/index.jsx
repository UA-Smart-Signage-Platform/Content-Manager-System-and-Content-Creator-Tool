import { NavBar } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";

function Root(){
    const theme = useThemeStore((state) =>state.theme)
    console.log(theme)

    return(
        <>
            <NavBar/>
            <div className={` pl-[65px] h-full ${theme}`}>
                <Outlet/>
            </div>
        </>
    )
}

export default Root;