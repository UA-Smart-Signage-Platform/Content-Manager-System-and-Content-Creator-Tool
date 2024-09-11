import { AnimatePresence, motion } from 'framer-motion';
import ScheduleModal from './ScheduleModal';

function ModalWithBlur({ setShowPortal, selectedGroup, updater, setUpdater, totalRules, titleMessage, ruleId, setRuleId, edit, setEdit }) {
    return createPortal(
        <motion.div key="background"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    transition={{ duration:0.3 }}
                    className="fixed z-20 top-0 h-screen w-screen backdrop-blur-sm flex">
            <div className="bg-black h-screen w-screen opacity-75"/>
            <motion.div key="content"
                        initial={{ scale: 0.8 }}
                        animate={{ scale: 1 }}
                        exit={{ scale: 0.8 }}
                        transition={{ duration: 0.3, ease: "easeOut" }}
                        className="absolute text-gray-50 h-screen w-screen flex items-center">
                <ScheduleModal  setShowPortal={setShowPortal}
                                selectedGroup={groupsQuery.data.data.find(x => x.id === selectedGroupId + 1)}
                                updater={updater}
                                setUpdater={setUpdater}
                                totalRules={rulesByGroupIdQuery.data.data.length}
                                titleMessage={scheduleModalTitle}
                                ruleId={ruleId}
                                setRuleId={setRuleId}
                                edit={edit}
                                setEdit={setEdit}/>
            </motion.div>
        </motion.div>
    )
}

export default ModalWithBlur;