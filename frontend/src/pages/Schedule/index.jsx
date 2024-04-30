import { useEffect, useState } from "react";
import { PageTitle, ScheduleModal } from "../../components";
import monitorsGroupService from "../../services/monitorsGroupService";
import { motion } from "framer-motion"

function Schedule(){
    const [groups, setGroups] = useState([]);
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [showPortal, setShowPortal] = useState(false);

    useEffect(() => {
        monitorsGroupService.getGroups().then((response) => {
            setGroups(response.data);
        })
    }, []);

    return(
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"schedule"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex h-[92%] mr-3 ml-3 ">
                <div className="flex flex-col w-[25%] h-full pt-4">
                    <div className="flex flex-row w-full h-[5%] items-center ">
                        <motion.button 
                            whileHover={{ 
                                scale: 1.1, 
                                border: "2px solid", 
                                transition: {
                                    duration: 0.2,
                                    ease: "easeInOut",
                                }, 
                            }}
                            whileTap={{ scale: 0.9 }}
                            onClick={() => {selectedGroupId !== null && setShowPortal(true)}} 
                            className="bg-secondaryLight rounded-md h-[80%] pr-4 pl-4">
                            + Add rule
                        </motion.button>
                        <motion.select
                            whileHover={{ border: "2px solid" }}
                            whileTap={{ border: "2px solid" }}
                            onChange={(e) => setSelectedGroupId(e.target.value - 1)} 
                            className="ml-auto mr-5 bg-secondaryLight rounded-md h-[80%] pr-3 pl-3 cursor-pointer">
                            <option selected disabled hidden>Group</option>
                            {groups.length !== 0 && groups.map((group) => 
                                <option value={group.id}>{group.name}</option>
                            )}
                        </motion.select>
                    </div>
                    {showPortal && <ScheduleModal
                            showPortal={showPortal} 
                            setShowPortal={setShowPortal}
                            selectedGroup={groups.at(selectedGroupId)} />
                    }
                    <div className="w-full h-[90%]">
                        <div className="bg-secondaryLight"></div>
                    </div>
                </div>
                <div id="dividerHr" className="mt-1 mb-1 w-[1px] h-full border-[1px] border-secondary"/>
                <div className="w-[74%] bg-secondaryLight ml-3 mt-3">

                </div>
            </div>
        </div>
    )
}

export default Schedule;