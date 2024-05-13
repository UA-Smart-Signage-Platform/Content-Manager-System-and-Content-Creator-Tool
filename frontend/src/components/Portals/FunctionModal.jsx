import { motion } from "framer-motion";
import { createPortal } from "react-dom";
import PropTypes from 'prop-types';

function FunctionModal( { cancelFunc,funcToExecute,message,confirmMessage } ) {
    return createPortal (
        <motion.div key="backgroundContents"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }} 
                className="fixed z-20 top-0 h-screen w-screen backdrop-blur-sm flex">
            <div className="bg-black h-screen w-screen opacity-75"></div>
            <motion.div key="contents"
                initial={{ scale: 0.8 }}
                animate={{ scale: 1 }}
                exit={{ scale: 0.8 }}
                transition={{ duration: 0.3, ease: "easeOut" }}
                className="absolute text-gray-50 h-screen w-screen flex items-center">
                <div className="bg-[#fafdf7] text-[#101604] h-[35%] w-[25%] mx-auto rounded-xl p-[2%] flex flex-col">
                    <div className="flex h-[50%] place-content-center items-center text-center">
                        <span className="text-2xl">{message}</span>
                    </div>
                    <div className="flex flex-row h-[50%] justify-evenly items-center">
                        <div>
                            <button onClick={funcToExecute} className="flex bg-[#D12E2E] text-white rounded-md p-2 pr-5 pl-5">
                                <span className="text-xl">{confirmMessage}</span>
                            </button>
                        </div>
                        <div>
                            <button onClick={cancelFunc} className="flex bg-[#d7dad6] rounded-md p-2 pr-5 pl-5">
                                <span className="text-xl">Cancel</span>
                            </button>
                        </div>
                    </div>
                </div>
            </motion.div>
        </motion.div>,
    document.body
    );
}

FunctionModal.propTypes = {
    cancelFunc: PropTypes.func.isRequired,
    message: PropTypes.string,
    funcToExecute: PropTypes.func.isRequired,
}

export default FunctionModal;