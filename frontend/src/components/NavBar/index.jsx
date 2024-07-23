import { MdOutlineDashboard,MdOutlinePermMedia,
    MdCalendarToday,MdMonitor,
    MdOutlineBrandingWatermark,MdOutlineWbSunny, MdOutlineSupervisorAccount, MdOutlineLogout } from "react-icons/md";
import { LuMoon } from "react-icons/lu";
import  Logo  from "../../static/logo.svg?react"
import { AnimatePresence, motion } from "framer-motion";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useThemeStore } from "../../stores/useThemeStore";
import PropTypes from 'prop-types';


function NavBar({setLogged}) {
    const [isShow,setIsShow] = useState(false);
    const theme = useThemeStore((state) =>state.theme)
    const changeTheme = useThemeStore((state)=> state.changeTheme)
    const navigate = useNavigate();
    const isLoggedIn = localStorage.getItem('access_token');
    const userInfo = localStorage.getItem('userInfo');
    const userRole = userInfo ? JSON.parse(userInfo).role : null;


    const handleLogout = () => {
        localStorage.removeItem('access_token');
        localStorage.removeItem('id_token');
        localStorage.removeItem('refresh_token');
        localStorage.removeItem('userInfo');
        navigate("/Login");
        setLogged(false);
    };

    return(
        <> 
        <div className=" flex flex-col w-[65px] bg-black h-[100vh] gap-10 z-10 text-white fixed"
             onMouseEnter={()=>{setIsShow(true);}}
             onMouseLeave={()=>{setIsShow(false)}}>
            <div className="w-full h-[16%]">
                <Logo className="w-full h-full pl-2 pr-1"></Logo>
            </div>
            <Link to={"dashboard"} className="w-full h-[5%]">
                <MdOutlineDashboard className=" h-full w-full px-3.5 text-white"/>
            </Link>
            <Link to={"media/home"} className="w-full h-[5%]">
                <MdOutlinePermMedia className=" h-full w-full px-3.5"/>
            </Link>
            <Link to={"schedule"} className="w-full h-[5%]">
                <MdCalendarToday className=" h-full w-full px-3.5"/>
            </Link>
            <Link to={"monitors"} className="w-full h-[5%]">
                <MdMonitor className=" h-full w-full px-3.5"/>
            </Link>
            {userRole === "ROLE_ADMIN" && (
                <Link to={"contentcreator"} className="w-full h-[5%]">
                    <MdOutlineBrandingWatermark className=" h-full w-full px-3.5"/>
                </Link>
            )}
            {userRole === "ROLE_ADMIN" && (
                <Link to={"admin"} className="w-full h-[5%]">
                    <MdOutlineSupervisorAccount className=" h-full w-full px-3.5"/>
                </Link>
            )}
            {isLoggedIn && (
                <button className="w-full h-[5%] text-lg justify-evenly cursor-pointer" onClick={handleLogout}>
                    <MdOutlineLogout className=" h-full w-full px-3.5"/>
                </button>
            )}
            
            
        </div>
        <AnimatePresence>
            {isShow && <motion.div className=" flex flex-col w-[10%] bg-black h-[100vh] gap-10 z-0 text-white fixed left-[65px] font-Lexend text-2xl font-semibold"
                initial={{x:-200}}
                animate={{x:0}}
                exit={{x:-200}}
                transition={{ease:"easeInOut",duration:0.4}}
                onMouseEnter={()=>{setIsShow(true);}}
                onMouseLeave={()=>{setIsShow(false)}}
                >
                <div className="w-full h-[16%] flex flex-col place-content-center font-bold">
                    <h1>UA SMART</h1>
                    <h1 className="ml-8">SIGNAGE</h1> 
                </div>
                <Link to={"dashboard"} className="w-full h-[5%] flex items-center">
                    Dashboard
                </Link>
                <Link to={"media/home"} className="w-full h-[5%] flex items-center">
                    Media
                </Link>
                <Link to={"schedule"} className="w-full h-[5%] flex items-center">
                    Schedule
                </Link>
                <Link to={"monitors"} className="w-full h-[5%] flex items-center">
                    Monitors
                </Link>
                {userRole === "ROLE_ADMIN" && (
                    <Link to={"contentcreator"} className="w-full h-[5%] flex items-center">
                        Content Creator Tool
                    </Link>
                )}
                {userRole === "ROLE_ADMIN" && (
                    <Link to={"admin"} className="w-full h-[5%] flex items-center">
                        Users
                    </Link>
                )}
                {isLoggedIn && ( 
                    <button className="w-full h-[5%] flex items-center" onClick={handleLogout}>
                        Logout
                    </button>
                )}

                <button className="w-full h-[5%] flex items-center mt-auto text-lg justify-evenly cursor-pointer" onClick={() => changeTheme(theme === "dark" ? "light" : "dark")}>
                    <div className="h-full w-[25px] relative flex items-center">
                        {theme === "light" && (
                            <motion.div
                                className="absolute"
                                initial={{ y: -10, opacity: 0, rotate: 90 }}
                                animate={{ y: 0, opacity: 1, rotate: 0 }}
                                exit={{ y: -10, opacity: 0, rotate: 90 }}
                            >
                                <MdOutlineWbSunny className="h-full w-[25px]" />
                            </motion.div>
                        )}
                        {theme === "dark" && (
                            <motion.div
                                className="absolute"
                                initial={{ y: 10, opacity: 0, rotate: -90 }}
                                animate={{ y: 0, opacity: 1, rotate: 0 }}
                                exit={{ y: 10, opacity: 0, rotate: -90 }}
                            >
                                <LuMoon className="h-full w-[25px]" />
                            </motion.div>
                        )}
                    </div>
                    Change Theme
                </button>
            </motion.div>}
        </AnimatePresence>
        </>
    )
}

NavBar.propTypes = {
    setLogged: PropTypes.func.isRequired,
};

export default NavBar;