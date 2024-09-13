import { useState } from "react";
import { PageTitle, ScheduleModal, FunctionModal } from "../../components";
import monitorsGroupService from "../../services/monitorsGroupService";
import ruleService from "../../services/ruleService";
import { AnimatePresence, motion } from "framer-motion"
import { ALL_WEEKDAYS } from 'rrule'
import { IoMdArrowDown, IoMdArrowUp } from "react-icons/io";
import { FiTrash2 } from "react-icons/fi";
import scheduleService from "../../services/scheduleService";
import activeTemplateService from "../../services/activeTemplateService";
import { useMutation, useQueries } from '@tanstack/react-query';
import { priorityDown, priorityUp } from "./priorityConfig";
import { addRuleButton, cancelButton, saveButton, selectGroupButton } from "./buttonsConfig";
import { warningPopUp } from "./utilsConfig";

function Schedule(){
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [showPortal, setShowPortal] = useState(false);
    const [showGroupNeeded, setShowGroupNeeded] = useState(false);
    const [updater, setUpdater] = useState(false);
    const [changesMade, setChangesMade] = useState(false);
    const [scheduleModalTitle, setScheduleModalTitle] = useState("");
    const [ruleId, setRuleId] = useState(null);
    const [edit, setEdit] = useState(false);
    const [rulesToDelete, setRulesToDelete] = useState([]);


    const [groupsQuery, rulesByGroupIdQuery] = useQueries({
        queries : [
            {
                queryKey: ['groups'],
                queryFn: () => monitorsGroupService.getGroups()
            },
            {
                queryKey: ['rules', selectedGroupId, updater],
                queryFn: () => ruleService.getRulesByGroupId(selectedGroupId),
                enabled: selectedGroupId !== null
            }
        ]
    });

    console.log(rulesByGroupIdQuery.data?.data.map(rule => rule.schedule.priority))

    const handleSave = async () => {
        const idsArr = rulesToDelete.map(rule => rule.id);

        const deletePromises = idsArr.map(ruleId => ruleService.deleteRule(ruleId));
    
        const rulesArr = rulesByGroupIdQuery.data.data;
    
        const updatePromises = rulesArr.map(rule => ruleService.updateRule(rule.id, {groupId: rule.groupId, 
                                                                                        templateId: rule.template.id, 
                                                                                        schedule: rule.schedule,
                                                                                        chosenValues: rule.chosenValues}));
    
        await Promise.all([...deletePromises, ...updatePromises]);
    
        setChangesMade(false);
        setUpdater(!updater);
    };
    
    const handleUpdateSingleRule = (ruleId) => {
        setScheduleModalTitle("Editing rule for ");
        setRuleId(ruleId);
        setEdit(true);
        setShowPortal(true);
    }
    
    const deleteRule = (ruleToDelete) => {
        setRulesToDelete(previousRules => [...previousRules, ruleToDelete]);
    
        const ruleToDeletePriority = ruleToDelete.schedule.priority;
    
        let arr = rulesByGroupIdQuery.data.data.filter(rule => rule.schedule.priority !== ruleToDeletePriority).map((rule)=>{
            if (rule.schedule.priority > ruleToDeletePriority){
                rule.schedule.priority = rule.schedule.priority - 1;
            }
            return rule
        })
    
        rulesByGroupIdQuery.data.data = arr;
        setChangesMade(true);
    }
    

    const displayRules = () => {
        if (selectedGroupId === null){
            return(
                <div className="flex pt-[15%] place-content-center w-full h-full">
                    <span className="pt-[10%] text-xl font-light">Select a group to see its rules</span>
                </div>
            );
        }
        else if (rulesByGroupIdQuery.data.data.length === 0){
            return(
                <div className="flex pt-[15%] place-content-center w-full h-full">
                    <span className="pt-[10%] text-xl font-light">This group contains no rules</span>
                </div>
            );
        }
        else {
            return(
                <div className="relative w-full h-full">
                    {[].concat(rulesByGroupIdQuery.data.data).sort((a,b) => {return b.schedule.priority < a.schedule.priority}).map((rule, index) => (
                        <motion.div 
                            animate={{y:index * 96}}
                            key={rule.id} 
                            className="flex w-full p-2 absolute h-24">
                            <button onClick={() => handleUpdateSingleRule(`${rule.id}`)}
                                className="w-[85%] bg-secondaryLight rounded-l-md pl-2 pb-2 text-textcolorNotSelected place-content-center cursor-pointer">
                                <span className="text-textcolor">{rule.template.name}</span> running {/* */}
                                <span className="text-textcolor">weekly</span> from {/* */}
                                <span className="text-textcolor">{rule.schedule.startTime[0]}:{rule.schedule.startTime[1]}</span> to {/* */}
                                <span className="text-textcolor">{rule.schedule.endTime[0]}:{rule.schedule.endTime[1]}</span><br/> during {/* */}
                                {rule.schedule.weekdays.map((day, index) => (
                                    <span key={ALL_WEEKDAYS[day]} className="text-textcolor">{ALL_WEEKDAYS[day]}{index < rule.schedule.weekdays.length - 1 && <>,</>}{/* */} </span>
                                ))}
                            </button>
                            <div className="flex w-[15%] bg-secondaryLight rounded-r-md">
                                <div id="dividerHr" className="w-[1px] h-full border-[1px] border-secondaryMedium"/>
                                <div className="flex flex-col w-full gap-2 m-auto">
                                        <div className="flex flex-row  gap-2 m-auto">
                                            <motion.button onClick={() => priorityDown(rule, rulesByGroupIdQuery, setChangesMade)} whileHover={{scale:1.2}}
                                                    className=" border border-black rounded size-5 flex justify-center items-center">
                                                <IoMdArrowDown/>
                                            </motion.button>
                                            <motion.button onClick={() => priorityUp(rule, rulesByGroupIdQuery, setChangesMade)} whileHover={{scale:1.2}}
                                                    className=" border border-black rounded size-5 flex justify-center items-center">
                                                <IoMdArrowUp/>
                                            </motion.button>
                                        </div>
                                        <div className="flex items-center object-center place-content-center">
                                            <motion.button onClick={() => {deleteRule(rule)}} whileHover={{scale:1.2}}
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

    if (!groupsQuery.isLoading && !rulesByGroupIdQuery.isLoading){
        return(
            <div className="h-full flex flex-col">
                <div id="title" className="pt-4 h-[8%]">
                    <PageTitle startTitle={"schedule"} 
                                middleTitle={"default"}
                                endTitle={"dashboard"}/>
                </div>
                <div id="divider" className="flex h-[92%] mr-3 ml-3 ">
                    <div className="flex flex-col w-[25%] h-full pt-4">
                        <div className="flex flex-row w-full h-[5%]">
                            <div className="flex w-[50%] h-full items-center">
                                {addRuleButton(changesMade, selectedGroupId, setShowGroupNeeded, setShowPortal, setScheduleModalTitle)}
                            </div>
                            <div className="flex w-[50%] h-full items-center relative">
                                {saveButton(selectedGroupId, changesMade, handleSave)}
                                {cancelButton(selectedGroupId, changesMade, setChangesMade, setUpdater, updater)}
                                {selectGroupButton(selectedGroupId, setSelectedGroupId, setShowGroupNeeded, setChangesMade, groupsQuery)}
                                {warningPopUp(showGroupNeeded)}
                            </div>
                        </div>
                        <AnimatePresence>
                            {showPortal && !changesMade && <ScheduleModal
                                    setShowPortal={setShowPortal}
                                    selectedGroup={groupsQuery.data.data.find(x => x.id == selectedGroupId)}
                                    updater={updater}
                                    setUpdater={setUpdater}
                                    totalRules={rulesByGroupIdQuery.data.data.length}
                                    titleMessage={scheduleModalTitle}
                                    ruleId={ruleId}
                                    setRuleId={setRuleId}
                                    edit={edit}
                                    setEdit={setEdit} />
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
    
}

export default Schedule;