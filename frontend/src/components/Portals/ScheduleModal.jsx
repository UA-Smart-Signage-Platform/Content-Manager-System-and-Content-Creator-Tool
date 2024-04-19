import { createPortal } from 'react-dom';
import { MdArrowBack } from "react-icons/md";
import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import monitorsGroupService from "../../services/monitorsGroupService";
import templateService from "../../services/templateService";
import activeTemplateService from "../../services/activeTemplateService";
import ScheduleContentModal from './ScheduleContentModal';

function ScheduleModal( { showPortal, setShowPortal } ) {
    const [templates, setTemplates] = useState([]);
    const [groups, setGroups] = useState([]);
    const [selectColors,setSelectedColors] = useState([]);

    const [selectedTemplateId, setSelectedTemplateId] = useState(null);
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [selectedContent, setSelectedContent] = useState({});

    const [isDropdownOpen, setDropdownOpen] = useState(-1);

    const [showContentsPortal, setShowContentsPortal] = useState(false);

    useEffect(() => {
        monitorsGroupService.getGroups().then((response) => {
            setGroups(response.data);
        });

        templateService.getTemplates().then((response) => {
            setTemplates(response.data);
        })
    }, []);


    const handleSubmit = () => {
        const data = {
            template: { id: selectedTemplateId },
            group: { id: selectedGroupId },
            content: { selectedContent },
        }

        activeTemplateService.changeActiveTemplate(data);
    }

    const colors = [
        'bg-pink-200 border-pink-400',
        'bg-rose-200 border-rose-400',
        'bg-violet-200 border-violet-400',
        'bg-blue-200 border-blue-400',
        'bg-green-200 border-blue-400',
        'bg-yellow-200 border-yellow-400',
        'bg-orange-200 border-orange-400',
        'bg-stone-200 border-stone-400'
    ];

    useEffect(()=>{
        if (templates.length !== 0){
            let template = templates.at(selectedTemplateId-1).templateWidgets.length;
            let arr = [];
            for (let i = 0; i < template; i++){
                arr.push(colors[Math.floor(Math.random() * colors.length)]);
            }
            setSelectedColors(arr)
        }

    },[selectedTemplateId])

    return (
    <>
        {showPortal && createPortal(
            <div className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                <div className="bg-black h-screen w-screen opacity-75"></div>
                <div className="absolute text-gray-50 h-screen w-screen flex items-center">
                    <div className="bg-[#fafdf7] text-[#101604] h-[90%] w-[90%] mx-auto rounded-xl p-[1%]">
                        <div className="h-[5%] w-full flex items-center">
                            <button onClick={() => setShowPortal(false)} className="flex flex-row">
                                <MdArrowBack className="w-7 h-7 mr-2"/> 
                                <span className="text-xl">Go back</span>
                            </button>
                        </div>
                        <div className="h-[90%] w-full p-[1%] flex flex-row">
                            <div className="w-[30%] text-xl">
                                <div className="w-full h-full flex flex-col items-center place-content-center">
                                    <span className="text-2xl">Creating new rule for:</span>
                                    <div className="flex flex-row mt-5">
                                        <select onChange={(e) => setSelectedGroupId(e.target.value)} className="bg-zinc-300 rounded-md pr-3 pl-3 cursor-pointer mr-8">
                                            <option selected disabled hidden>Group</option>
                                            {groups.length !== 0 && groups.map((group) => 
                                                <option value={group.id}>{group.name}</option>
                                            )}
                                        </select>
                                        <select id="templateSelect" onChange={(e) => setSelectedTemplateId(e.target.value)} className="bg-zinc-300 rounded-md p-2 cursor-pointer">
                                            <option selected disabled hidden>Template</option>
                                            {templates.length !== 0 && templates.map((template) => 
                                                <option value={template.id}>{template.name}</option>
                                            )}
                                        </select>
                                    </div>
                                    <div className="flex place-self-end mr-auto ml-auto text-xl">
                                        <button onClick={handleSubmit} className="bg-blue-300 rounded-md p-3 mt-6">Submit</button>
                                    </div>
                                </div>
                            </div>
                            <div></div>
                            <div className="w-[70%] text-lg flex flex-col">
                                <div className="flex w-full h-[8%] items-center place-items-center">
                                    <span className="font-medium text-3xl">Template Preview:</span>
                                </div>
                                <div className="w-full h-[92%] relative border-4 border-gray-300 rounded-md">
                                    {selectedTemplateId !== null && selectedGroupId !== null && templates[selectedTemplateId - 1].templateWidgets.map((templateWidget, index) => 
                                    <div className={`absolute`}
                                        style={{
                                            width: `${templateWidget.width}%`,
                                            height: `${templateWidget.height}%`,
                                            top: `${templateWidget.top}%`,
                                            left: `${templateWidget.leftPosition}%`
                                        }}> 
                                        <button onClick={(e) => {setDropdownOpen(isDropdownOpen > -1 ? -1 : index);}} 
                                                className={`h-full w-full absolute flex flex-col items-center place-content-center border-2 rounded-sm ${selectColors[index]}`}>
                                            <span className='z-10'>{templateWidget.widget.name}</span>
                                            {isDropdownOpen === index && (
                                                    <div className="text-md bg-slate-300 rounded-md">
                                                        <ul>
                                                            <li onClick={() => { setShowContentsPortal(true); setDropdownOpen(-1)}} className="pl-1 pb-2 cursor-pointer">Add File</li>
                                                            <li className="pl-1 pb-2 cursor-pointer">Add Folder</li>
                                                        </ul>
                                                    </div>
                                            )}
                                        </button>
                                    </div>
                                    )}
                                    <ScheduleContentModal
                                        showContentsPortal={showContentsPortal} 
                                        setShowContentsPortal={setShowContentsPortal} />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>,
        document.body
        )}
    </>
    )
}


ScheduleModal.propTypes = {
    showPortal: PropTypes.bool.isRequired,
    setShowPortal: PropTypes.func.isRequired
}

export default ScheduleModal;