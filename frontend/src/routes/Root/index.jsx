import { NavBar } from "../../components";
import { Outlet } from "react-router";

function Root(){

    return(
        <div className=" flex">
            <NavBar/>
            <div className=" pl-[65px] h-full">
                <Outlet/>
            </div>
        </div>
    )
}

export default Root;