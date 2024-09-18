import { useState } from "react";
import { PageTitle, ScheduleModal } from "../../components";
import monitorsGroupService from "../../services/monitorsGroupService";
import ruleService from "../../services/ruleService";
import { AnimatePresence, motion } from "framer-motion"
import { ALL_WEEKDAYS } from 'rrule'
import { IoMdArrowDown, IoMdArrowUp } from "react-icons/io";
import { FiTrash2 } from "react-icons/fi";
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
    const [priorityChanged, setPriorityChanged] = useState(false);
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

    const deleteRuleMutation = useMutation({
        mutationFn: (id) => ruleService.deleteRule(id),
    });

    const updateRuleMutation = useMutation({
        mutationFn: (rule) => ruleService.updateRule(rule.id, {
            groupId: rule.groupId,
            templateId: rule.template.id,
            schedule: rule.schedule,
            chosenValues: rule.chosenValues,
        })
    });

    const { mutate: deleteRuleMutate } = deleteRuleMutation;
    const { mutate: updateRuleMutate } = updateRuleMutation;

    const handleSave = async () => {
        const idsArr = rulesToDelete.map(rule => rule.id);

        const deletePromises = idsArr.map(ruleId => deleteRuleMutate(ruleId));

        const rulesArr = rulesByGroupIdQuery.data.data;

        const updatePromises = rulesArr.map(rule => updateRuleMutate(rule));

        await Promise.all([...deletePromises, ...updatePromises]);

        setChangesMade(false);
        setRulesToDelete([]);
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
                <div className="w-full h-full p-2 relative">
                    {[].concat(rulesByGroupIdQuery.data.data).sort((a,b) => {return b.schedule.priority < a.schedule.priority}).map((rule, index) => (
                        <motion.div 
                            key={rule.id} 
                            initial={{x:0,y:0}}
                            animate={{x:0,y:index * 120 }}               
                            className="flex w-full p-2 bg-white rounded-lg shadow-md mb-4 absolute">
                            <button
                                onClick={() => handleUpdateSingleRule(`${rule.id}`)}
                                className="w-[80%] bg-secondaryLight hover:bg-secondaryDark transition-all duration-200 rounded-l-lg p-4 text-left text-textcolorNotSelected cursor-pointer"
                            >
                                <div className="font-semibold text-textcolor">{rule.template.name}</div>
                                <div className="text-sm text-textcolor">
                                    Running <span className="font-bold">weekly</span> from{" "}
                                    <span className="font-bold">
                                        {rule.schedule.startTime[0]}:{rule.schedule.startTime[1]}
                                    </span>{" "}
                                    to{" "}
                                    <span className="font-bold">
                                        {rule.schedule.endTime[0]}:{rule.schedule.endTime[1]}
                                    </span>{" "}
                                    <br />
                                    During:{" "}
                                    {rule.schedule.weekdays.map((day, index) => (
                                        <span key={ALL_WEEKDAYS[day]} className="inline-block">
                                            {ALL_WEEKDAYS[day]}
                                            {index < rule.schedule.weekdays.length - 1 && <>, </>}
                                        </span>
                                    ))}
                                </div>
                            </button>
                            <div className="flex w-[20%] bg-secondaryLight rounded-r-lg items-center justify-center">
                                <div id="dividerHr" className="w-[1px] mr-2 h-full border-[1px] border-secondaryMedium"/>
                                <div className="flex flex-col space-y-2">
                                    <motion.button
                                        onClick={() => priorityUp(rule, rulesByGroupIdQuery, setChangesMade, setPriorityChanged)}
                                        whileHover={{ scale: rule.schedule.priority === 0 ? 1 : 1.1 }} 
                                        disabled={rule.schedule.priority === 0}
                                        className={`p-2 rounded-full shadow-md transition-all duration-200 
                                            ${rule.schedule.priority === 0 ? 'bg-secondaryLight text-gray-500 cursor-not-allowed' : 'bg-primary hover:bg-green-500 text-white'}`}
                                    >
                                        <IoMdArrowUp />
                                    </motion.button>
                                    <motion.button
                                        onClick={() => priorityDown(rule, rulesByGroupIdQuery, setChangesMade, setPriorityChanged)}
                                        whileHover={{ scale: rule.schedule.priority + 1 === rulesByGroupIdQuery.data.data.length ? 1 : 1.1 }}
                                        disabled={rule.schedule.priority + 1 === rulesByGroupIdQuery.data.data.length}
                                        className={`p-2 rounded-full shadow-md transition-all duration-200 
                                            ${rule.schedule.priority + 1 === rulesByGroupIdQuery.data.data.length ? 'bg-secondaryLight text-gray-500 cursor-not-allowed' : 'bg-primary hover:bg-green-500 text-white'}`}
                                    >
                                        <IoMdArrowDown />
                                    </motion.button>
                                </div>
                                <motion.button
                                    onClick={() => deleteRule(rule)}
                                    whileHover={{ scale: 1.1 }}
                                    className="ml-4 p-2 bg-secondaryLight hover:bg-secondary text-gray-700 rounded-full shadow-md transition-all duration-200"
                                >
                                    <FiTrash2 />
                                </motion.button>
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