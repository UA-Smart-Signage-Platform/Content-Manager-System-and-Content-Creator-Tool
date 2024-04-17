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

    const [selectedTemplateId, setSelectedTemplateId] = useState(null);
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [selectedContent, setSelectedContent] = useState({});

    const [isDropdownOpen, setDropdownOpen] = useState(false);
    const [dropdownInfo, setDropdownInfo] = useState({ top: 0, left: 0, id: 0 });

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
        'bg-pink-700',
        'bg-rose-700',
        'bg-violet-700',
        'bg-blue-700',
        'bg-green-700',
        'bg-yellow-700',
        'bg-orange-700',
        'bg-stone-700'
    ];

    function getRandomColorClass() {
        return colors[Math.floor(Math.random() * colors.length)];
    }



    return (
    <>
        {showPortal && createPortal(
            <div className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                <div className="bg-black h-screen w-screen opacity-75"></div>
                <div className="absolute text-gray-50 h-screen w-screen flex items-center">
                    <div className="bg-[#fafdf7] text-[#101604] h-[75%] w-[70%] mx-auto rounded-xl p-[1%]">
                        <div className="h-[5%] w-full flex items-center">
                            <button onClick={() => setShowPortal(false)} className="flex flex-row">
                                <MdArrowBack className="w-7 h-7 mr-2"/> 
                                <span className="text-xl">Go back</span>
                            </button>
                        </div>
                        <div className="h-[90%] w-full p-[1%] flex flex-row">
                            <div className="w-[30%] text-xl">
                                <div className="w-full h-[50%] flex flex-col items-center place-content-center">
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
                                </div>
                                <div className="flex flex-col w-full h-[50%] pl-[15%]">
                                    <span className="text-2xl h-[10%]">Content:</span>
                                    <div className="flex flex-row justify-around h-[75%] mt-5">
                                        {selectedTemplateId !== null && selectedGroupId !== null && templates[selectedTemplateId - 1].templateWidgets.map((templateWidget) => 
                                        <div>
                                            {templateWidget.widget.contents.length !== 0 && 
                                                <div className="flex flex-col">
                                                    <span>{templateWidget.widget.name}</span>
                                                    <button onClick={(e) => {setDropdownOpen(!isDropdownOpen);                         
                                                                            const buttonRect = e.target.getBoundingClientRect();
                                                                            setDropdownInfo({ top: buttonRect.bottom, 
                                                                                            left: buttonRect.left, 
                                                                                            id: `${templateWidget.widget.contents[0].id}` });}} 
                                                                            className=" bg-slate-200 rounded-md p-2">add content</button>
                                                </div>
                                            }
                                        </div>
                                        )}
                                        {isDropdownOpen && (
                                            <div className="fixed text-md z-10 bg-slate-300 w-[7%] rounded-md" style={{ top: dropdownInfo.top, left: dropdownInfo.left }}>
                                                <ul>
                                                    <li onClick={() => { setShowContentsPortal(true); setDropdownOpen(!isDropdownOpen)}} className="pl-1 pb-2 cursor-pointer">Add File</li>
                                                    <li className="pl-1 pb-2 cursor-pointer">Add Folder</li>
                                                </ul>
                                            </div>
                                        )}
                                        <ScheduleContentModal
                                            showContentsPortal={showContentsPortal} 
                                            setShowContentsPortal={setShowContentsPortal} />
                                    </div>
                                    <div className="flex h-[15%] place-self-end mr-auto ml-auto text-xl">
                                        <button onClick={handleSubmit} className="bg-blue-300 rounded-md p-3">Submit</button>
                                    </div>
                                </div>
                            </div>
                            <div></div>
                            <div className="w-[70%] text-lg flex flex-col">
                                <div className="flex w-full h-[8%] items-center place-items-center">
                                    <span className="font-medium text-3xl">Template Preview:</span>
                                </div>
                                <div className="w-full h-[92%] relative top-[0%] left-[0%] border-4 border-gray-300">
                                    {selectedTemplateId !== null && selectedGroupId !== null && templates[selectedTemplateId - 1].templateWidgets.map((templateWidget) => 
                                    <div className={`absolute border-2`}
                                        style={{
                                            width: `${templateWidget.width}%`,
                                            height: `${templateWidget.height}%`,
                                            top: `${templateWidget.top}%`,
                                            left: `${templateWidget.leftPosition}%`
                                        }}> 
                                        <div className={`h-full w-full absolute ${getRandomColorClass()} opacity-25`}></div>
                                        <div className="h-full w-full flex items-center place-content-center">
                                            <span>{templateWidget.widget.name}</span>
                                        </div>
                                    </div>
                                    )}
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