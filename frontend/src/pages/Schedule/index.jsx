import { useEffect, useState } from "react";
import { PageTitle, ScheduleModal } from "../../components";
import monitorsGroupService from "../../services/monitorsGroupService";
import { AnimatePresence, motion } from "framer-motion"
import { ALL_WEEKDAYS } from 'rrule'
import { IoMdArrowDown, IoMdArrowUp } from "react-icons/io";
import { FiTrash2 } from "react-icons/fi";
import scheduleService from "../../services/scheduleService";
import activeTemplateService from "../../services/activeTemplateService";

function Schedule(){
    const [groups, setGroups] = useState([]);
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [showPortal, setShowPortal] = useState(false);
    const [showGroupNeeded, setShowGroupNeeded] = useState(false);
    const [updater, setUpdater] = useState(false);
    const [rules, setRules] = useState([]);
    const [changesMade, setChangesMade] = useState(false);

    useEffect(() => {
        monitorsGroupService.getGroups().then((response) => {
            setGroups(response.data);
        })
    }, []);

    useEffect(() => {
        if (selectedGroupId !== null){
            monitorsGroupService.getGroup(selectedGroupId + 1).then((response) => {
                setRules(response.data.templateGroups);
            })
        }
    }, [selectedGroupId, updater]);
    

    const priorityDown = (rule) => {
        const priority = rule.schedule.priority;

        if (priority === rules.length - 1){
            return;
        }

        let arr = rules.map((element) => {
            if (element.schedule.priority === priority){
                element.schedule.priority++;
            }
            else if (element.schedule.priority - 1 === priority){
                element.schedule.priority--;
            }
            return element;
        })

        setRules(arr);
        setChangesMade(true);
    };

    const priorityUp = (rule) => {
        const priority = rule.schedule.priority;

        if (priority === 0){
            return;
        }

        let arr = rules.map((element) => {
            if (element.schedule.priority === priority){
                element.schedule.priority--;
            }
            else if (element.schedule.priority + 1 === priority){
                element.schedule.priority++;
            }
            return element;
        })

        setRules(arr);
        setChangesMade(true);
    };

    const removeRule = (rule) => {
        activeTemplateService.deleteRule(rule.id).then(() => {
            setUpdater(!updater);
        });
    };

    const handleUpdateRules = () => {
        const arr = [];
        rules.forEach(element => {
            arr.push(element.schedule);
        });

        scheduleService.updateSchedule(arr);
        setChangesMade(false);
    };

    const displayRules = () => {
        if (selectedGroupId === null){
            return(
                <div className="flex pt-[15%] place-content-center w-full h-full">
                    <span className="pt-[10%] text-xl font-light">Select a group to see its rules</span>
                </div>
            );
        }
        else if (rules.length === 0){
            return(
                <div className="flex pt-[15%] place-content-center w-full h-full">
                    <span className="pt-[10%] text-xl font-light">This group contains no rules</span>
                </div>
            );
        }
        else {
            return(
                <div className="relative w-full h-full">
                    {[].concat(rules).sort((a,b) => {return b.schedule.priority < a.schedule.priority}).map((rule, index) => (
                        <motion.div 
                            animate={{y:index * 96}}
                            key={rule.id} 
                            className="flex w-full p-2 absolute h-24">
                            <div className="w-[85%] bg-secondaryLight rounded-l-md pl-2 pb-2 text-textcolorNotSelected place-content-center">
                                <span className="text-textcolor">{rule.template.name}</span> running {/* */}
                                <span className="text-textcolor">weekly</span> from {/* */}
                                <span className="text-textcolor">{rule.schedule.startTime[0]}:{rule.schedule.startTime[1]}</span> to {/* */}
                                <span className="text-textcolor">{rule.schedule.endTime[0]}:{rule.schedule.endTime[1]}</span><br/> during {/* */}
                                {rule.schedule.weekdays.map((day, index) => (
                                    <span key={ALL_WEEKDAYS[day]} className="text-textcolor">{ALL_WEEKDAYS[day]}{index < rule.schedule.weekdays.length - 1 && <>,</>}{/* */} </span>
                                ))}
                            </div>
                            <div className="flex w-[15%] bg-secondaryLight rounded-r-md">
                                <div id="dividerHr" className="w-[1px] h-full border-[1px] border-secondaryMedium"/>
                                <div className="flex flex-col w-full gap-2 m-auto">
                                        <div className="flex flex-row  gap-2 m-auto">
                                            <motion.button onClick={()=> priorityDown(rule)} whileHover={{scale:1.2}}
                                                    className=" border border-black rounded size-5 flex justify-center items-center">
                                                <IoMdArrowDown/>
                                            </motion.button>
                                            <motion.button onClick={()=> priorityUp(rule)} whileHover={{scale:1.2}}
                                                    className=" border border-black rounded size-5 flex justify-center items-center">
                                                <IoMdArrowUp/>
                                            </motion.button>
                                        </div>
                                        <div className="flex items-center object-center place-content-center">
                                            <motion.button onClick={()=>removeRule(rule)} whileHover={{scale:1.2}}
                                                    className=" border border-black rounded size-5 flex justify-center items-center">
                                                <FiTrash2 />
                                            </motion.button>
                                        </div>
                                </div>
                            </div>
                        </motion.div>
                    ))}
                </div>
            );
        }
    };


    return(
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"schedule"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex h-[92%] mr-3 ml-3 ">
                <div className="flex flex-col w-[25%] h-full pt-4">
                    <div className="flex flex-row w-full h-[5%]">
                        <div className="flex w-[50%] h-full items-center">
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
                                onClick={() => { selectedGroupId === null ? setShowGroupNeeded(true) : setShowPortal(true) }}
                                className="bg-secondaryLight rounded-md h-[80%] pr-4 pl-4">
                                + Add rule
                            </motion.button>
                        </div>
                        
                        <div className="flex w-[50%] h-full items-center relative">
                            {selectedGroupId !== null &&
                                <button disabled={!changesMade}
                                    onClick={() => handleUpdateRules()} 
                                    className={`bg-primary p-1 pr-4 pl-4 rounded-md ${changesMade ? "" : "opacity-45 cursor-not-allowed"}`}>
                                Save
                            </button>
                            }
                            <motion.select
                                whileHover={{ border: "2px solid" }}
                                whileTap={{ border: "2px solid" }}
                                onChange={(e) => {setSelectedGroupId(e.target.value - 1); setShowGroupNeeded(false); setChangesMade(false)}} 
                                className="ml-auto mr-5 bg-secondaryLight rounded-md h-[80%] pr-3 pl-3 cursor-pointer">
                                <option selected disabled hidden>Group</option>
                                {groups.length !== 0 && groups.map((group) => 
                                    <option value={group.id}>{group.name}</option>
                                )}
                            </motion.select>
                            {showGroupNeeded && 
                                <div className="absolute text-md text-red h-full top-10 right-1">
                                    You must select a group
                                </div>
                            }
                        </div>
                    </div>
                    <AnimatePresence>
                        {showPortal && <ScheduleModal
                                setShowPortal={setShowPortal}
                                selectedGroup={groups.at(selectedGroupId)}
                                updater={updater}
                                setUpdater={setUpdater}
                                totalRules={rules.length} />
                        }
                    </AnimatePresence>
                    <div className="flex flex-col w-full h-[95%] pt-3 overflow-scroll">
                        {displayRules()}
                    </div>
                </div>
                <div id="dividerHr" className="mt-1 mb-1 w-[1px] h-full border-[1px] border-secondary"/>
                <div className="w-[74%] bg-secondaryLight rounded-md ml-3 mt-3">

                </div>
            </div>
        </div>
    )
}

export default Schedule;