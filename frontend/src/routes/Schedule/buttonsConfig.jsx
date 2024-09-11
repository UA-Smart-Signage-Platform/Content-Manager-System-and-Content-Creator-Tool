import { motion } from "framer-motion"

export const addRuleButton = (changesMade, selectedGroupId, setShowGroupNeeded, setShowPortal, setScheduleModalTitle) => {
    return (
        <motion.button 
            whileHover={changesMade ? {} : { 
                scale: 1.1, 
                border: "2px solid", 
                transition: {
                    duration: 0.2,
                    ease: "easeInOut",
                }, 
            }}
            whileTap={changesMade ? {} : { scale: 0.9 }}
            onClick={() => { 
                if(!changesMade) 
                {selectedGroupId === null ? 
                setShowGroupNeeded(true) 
                : 
                setShowPortal(true); 
                setScheduleModalTitle("Creating new rule for ") 
                }
            }}
            className={`bg-secondaryLight rounded-md h-[80%] pr-4 pl-4 ${changesMade ? "disabled cursor-not-allowed opacity-70" : ""}`}>
            + Add rule
        </motion.button>
    )
}


export const saveButton = (selectedGroupId, changesMade, handleSave) => {
    if (selectedGroupId !== null){
        return (
            <button disabled={!changesMade}
                onClick={() => handleSave()} 
                className={`bg-primary p-1 pr-4 pl-4 rounded-md ${changesMade ? "" : "opacity-45 cursor-not-allowed"}`}>
                Save
            </button>
        )
    }
}


export const selectGroupButton = (setSelectedGroupId, setShowGroupNeeded, setChangesMade, groupsQuery) => {
    return (    
        <motion.select
            whileHover={{ border: "2px solid" }}
            whileTap={{ border: "2px solid" }}
            onChange={(e) => {setSelectedGroupId(e.target.value - 1); setShowGroupNeeded(false); setChangesMade(false)}} 
            className="ml-auto mr-5 bg-secondaryLight rounded-md h-[80%] pr-3 pl-3 cursor-pointer">
            <option selected disabled hidden>Group</option>
            {groupsQuery.data.data.length !== 0 && groupsQuery.data.data.map((group) => 
                <option key={group.id} value={group.id}>{group.name}</option>
            )}
        </motion.select>
    )
}