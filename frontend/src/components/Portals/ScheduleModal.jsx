import { createPortal } from 'react-dom';
import { MdArrowBack, MdArrowDropUp } from "react-icons/md";
import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import templateService from "../../services/templateService";
import activeTemplateService from '../../services/activeTemplateService';
import ScheduleContentModal from './ScheduleContentModal';
import Select from 'react-select'
import DatePicker from 'react-date-picker';
import 'react-date-picker/dist/DatePicker.css';
import 'react-calendar/dist/Calendar.css';
import { AnimatePresence, motion } from 'framer-motion';

const weekDays = [0, 1, 2, 3, 4, 5, 6];
const timeHour = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"];
const timeMinute = ["00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"];

const colors = [
    'bg-pink-200 border-pink-400',
    'bg-rose-200 border-rose-400',
    'bg-violet-200 border-violet-400',
    'bg-blue-200 border-blue-400',
    'bg-green-200 border-green-400',
    'bg-yellow-200 border-yellow-400',
    'bg-orange-200 border-orange-400',
    'bg-stone-200 border-stone-400'
];


function ScheduleModal( { setShowPortal, selectedGroup, updater, setUpdater } ) {
    const [templates, setTemplates] = useState([]);
    const [selectedColors,setSelectedColors] = useState([]);

    const [selectedTemplateId, setSelectedTemplateId] = useState(null);
    const [selectedDays, setSelectedDays] = useState([]);
    const [selectedStartTime, setSelectedStartTime] = useState([null, null]);
    const [selectedEndTime, setSelectedEndTime] = useState([null, null]);
    const [selectedStartDate, setSelectedStartDate] = useState(null);
    const [selectedEndDate, setSelectedEndDate] = useState(null);
    const [selectedContent, setSelectedContent] = useState({});

    const [showContentsPortal, setShowContentsPortal] = useState(false);
    const [selectedWidgetId, setSelectedWidgetId] = useState(null);

    const [displayInfo, setDisplayInfo] = useState(false);

    useEffect(() => {
        templateService.getTemplates().then((response) => {
            setTemplates(response.data);
        })
    }, []);


    useEffect(()=>{
        if (templates.length !== 0){
            let template = templates.at(selectedTemplateId-1).templateWidgets.length;
            let arr = [];
            for (let i = 0; i < template; i++){
                arr.push(colors[Math.floor(Math.random() * colors.length)]);
            }
            setSelectedColors(arr)
        }

    },[selectedTemplateId]);


    const handleSubmit = () => {
        Object.keys(selectedContent).forEach(key => {
            selectedContent[key] = selectedContent[key].id;
        });

        const data = {
            template: { id: selectedTemplateId },
            group: { id: selectedGroup.id },
            content: selectedContent,
            schedule: { weekdays : selectedDays,
                    startTime : selectedStartTime[0] + ":" + selectedStartTime[1],
                    endTime : selectedEndTime[0] + ":" + selectedEndTime[1],
                    startDate : selectedStartDate,
                    endDate : selectedEndDate}
        }

        activeTemplateService.addRule(data);
        setUpdater(!updater);
    };


    const handleSelectedDays = (event) => {
        const value = parseInt(event.target.value);

        if (selectedDays.includes(value)) {
            setSelectedDays(selectedDays.filter(day => day !== value));
        } 
        else {
            setSelectedDays([...selectedDays, value]);
        }
    };

    const handleSelectedContent = (event) => {
        setSelectedContent({...selectedContent, [event.value] : event.label });
    }


    const handleDisplayAllTime = () => {
        setSelectedStartTime(["00", "00"]);
        setSelectedEndTime(["23", "55"]);
        setSelectedDays(weekDays);
    };


    const handleDisplayWeeklyFrom8Till23 = () => {
        setSelectedStartTime(["08", "40"]);
        setSelectedEndTime(["23", "00"]);
        setSelectedDays(weekDays);
    };

    const contentElement = (templateWidget) => {
        if (templateWidget.widget.name === "Media") {
            return (
                <>
                    <span className='z-10'>{templateWidget.widget.name}</span>
                    <button 
                            onClick={() => { setShowContentsPortal(true); setSelectedWidgetId(`${templateWidget.id}`) }} 
                            className="bg-[#E9E9E9] border-2 border-black pl-4 pr-4 rounded-lg">
                        <span>...</span>
                    </button>
                    {selectedContent[templateWidget.id] !== undefined && (selectedContent[templateWidget.id]).name}
                </>
            );
        } 
        else if (templateWidget.widget.contents.length > 0) {
            return (
                <div className="text-sm min-w-[90%] max-w-[90%]">
                    <Select
                        className="z-20"
                        placeholder={templateWidget.widget.name + "..."}
                        isSearchable="true"
                        onChange={(e) => handleSelectedContent(e)}
                        options={templateWidget.widget.contents[0].options.map(option => ({ value: templateWidget.widget.id, label: option }))}
                        getOptionValue={(option) => option.label}
                    />
                </div>
            );
        } 
        else {
            return (
                <span>{templateWidget.widget.name}</span>
            );
        }
    }
    
    return createPortal(
            <motion.div key="background"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    transition={{ duration:0.3 }}
            className="fixed z-10 top-0 h-screen w-screen backdrop-blur-sm flex">
                <div className="bg-black h-screen w-screen opacity-75"></div>
                <motion.div key="content"
                    initial={{ scale: 0.8 }}
                    animate={{ scale: 1 }}
                    exit={{ scale: 0.8 }}
                    transition={{ duration: 0.3, ease: "easeOut" }}
                    className="absolute text-gray-50 h-screen w-screen flex items-center">
                    <div className="bg-[#fafdf7] text-[#101604] h-[90%] w-[90%] mx-auto rounded-xl p-[1%]">
                        <div className="h-[5%] w-full flex items-center">
                            <button onClick={() => setShowPortal(false)} className="flex flex-row">
                                <MdArrowBack className="w-7 h-7 mr-2"/> 
                                <span className="text-xl">Go back</span>
                            </button>
                        </div>
                        <div className="h-[90%] w-full flex flex-row">
                            <div className="w-[40%] text-xl">
                                <div className="w-full h-full flex flex-col items-center content-center place-items-center place-content-center">
                                    <span className="text-2xl">Creating new rule for <span className="font-medium">{selectedGroup.name}</span></span>
                                    <div className="text-lg flex flex-row w-full justify-evenly">
                                        <div className="flex pt-5">
                                            <select id="templateSelect" onChange={(e) => setSelectedTemplateId(e.target.value)} className="bg-[#E9E9E9] rounded-md p-2">
                                                <option selected disabled hidden>Template</option>
                                                {templates.length !== 0 && templates.map((template) => 
                                                    <option key={template.id} value={template.id}>{template.name}</option>
                                                )}
                                            </select>
                                        </div>
                                        <div className="flex pt-5">
                                            <select className="bg-[#E9E9E9] rounded-md p-2 cursor-pointer">
                                                <option selected disabled hidden>Default rules</option>
                                                <option onClick={handleDisplayAllTime}>Display 24/7</option>
                                                <option onClick={handleDisplayWeeklyFrom8Till23}>Weekly 08:40 - 23:00</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div className="h-[55%] w-full pt-[7%] pr-[12%] pl-[12%]">
                                        <div className="h-full w-full bg-[#E9E9E9] rounded-md">
                                            <div className="h-[25%] pt-3 w-full flex flex-row">
                                                <div className="h-full w-[50%] flex flex-col items-center justify-center">
                                                    <span className="pb-1">Start Time:</span>
                                                    <div className="flex items-center rounded-md border border-gray-300">
                                                        <select value={selectedStartTime[0]} 
                                                                onChange={(event) => setSelectedStartTime([event.target.value, selectedStartTime[1]])} 
                                                                className="p-2 pr-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeHour
                                                                .filter(hour => selectedEndTime[0] === null || hour < selectedEndTime[0])
                                                                .map((hour) => (
                                                                    <option key={hour} selected={selectedStartTime[0] === hour} value={hour}>
                                                                        {hour}
                                                                    </option>
                                                            ))}
                                                        </select>
                                                        <span className="mx-2">:</span>
                                                        <select value={selectedStartTime[1]} 
                                                                onChange={(event) => setSelectedStartTime([selectedStartTime[0], event.target.value])} 
                                                                className="p-2 pl-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeMinute.map((minute) => (
                                                                <option key={minute} selected={selectedStartTime[1] === minute} value={minute}>
                                                                    {minute}
                                                                </option>
                                                            ))}
                                                        </select>
                                                    </div>
                                                </div>
                                                <div className="h-full w-[50%] flex flex-col items-center justify-center">
                                                    <span className="pb-1">End Time:</span>
                                                    <div className="flex items-center rounded-md border border-gray-300">
                                                        <select value={selectedEndTime[0]} 
                                                                onChange={(event) => setSelectedEndTime([event.target.value, selectedEndTime[1]])} 
                                                                className="p-2 pr-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeHour
                                                                .filter(hour => selectedStartTime[0] === null || hour > selectedStartTime[0])
                                                                .map((hour) => (
                                                                    <option key={hour} selected={selectedEndTime[0] === hour} value={hour}>
                                                                        {hour}
                                                                    </option>
                                                            ))}
                                                        </select>
                                                        <span className="mx-2">:</span>
                                                        <select value={selectedEndTime[1]} 
                                                                onChange={(event) => setSelectedEndTime([selectedEndTime[0], event.target.value])} 
                                                                className="p-2 pl-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeMinute.map((minute) => (
                                                                <option key={minute} selected={selectedEndTime[1] === minute} value={minute}>
                                                                    {minute}
                                                                </option>
                                                            ))}
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="h-[75%] w-full flex">
                                                <div className="h-full w-full p-3">
                                                    <div className="h-[50%] w-full p-3 pr-[10%] pl-[10%] place-content-center">
                                                        <span>Weekdays:</span>
                                                        <div className="flex justify-around pt-3 text-sm">
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[0])} 
                                                                        value={weekDays[0]} type="checkbox" className="h-5 w-5" />
                                                                <span>MON</span>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[1])} 
                                                                        value={weekDays[1]} type="checkbox" className="h-5 w-5" />
                                                                <span>TUE</span>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[2])} 
                                                                        value={weekDays[2]} type="checkbox" className="h-5 w-5" />
                                                                <span>WED</span>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[3])} 
                                                                        value={weekDays[3]} type="checkbox" className="h-5 w-5" />
                                                                <span>THU</span>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[4])} 
                                                                        value={weekDays[4]} type="checkbox" className="h-5 w-5" />
                                                                <span>FRI</span>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[5])} 
                                                                        value={weekDays[5]} type="checkbox" className="h-5 w-5" />
                                                                <span>SAT</span>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[6])} 
                                                                        value={weekDays[6]} type="checkbox" className="h-5 w-5" />
                                                                <span>SUN</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div className="h-[50%] w-full flex flex-row">
                                                        <div className="h-full w-[50%] flex flex-col items-center place-content-center">
                                                            <span>Start date:</span>
                                                            <DatePicker 
                                                                onChange={(date) => setSelectedStartDate(date)} 
                                                                value={selectedStartDate} 
                                                                minDate={new Date()}
                                                                format="dd/MM/y"/>
                                                        </div>
                                                        <div className="h-full w-[50%] flex flex-col items-center place-content-center">
                                                            <span>End date:</span>
                                                            <DatePicker 
                                                                onChange={(date) => setSelectedEndDate(date)} 
                                                                value={selectedEndDate} 
                                                                minDate={selectedStartDate !== null ? selectedStartDate : new Date()}
                                                                format="dd/MM/y"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="flex text-xl h-[10%] pt-[6%] w-full items-center place-content-center ">
                                        {(selectedTemplateId !== null && 
                                            selectedDays.length !== 0 &&
                                            !selectedStartTime.includes(null) &&
                                            !selectedEndTime.includes(null)
                                            ) 
                                            ?
                                            <button onClick={handleSubmit} 
                                                className="bg-[#96d600] rounded-md p-2 pl-4 pr-4">
                                                Create rule
                                            </button>
                                            :
                                            <div
                                                onMouseEnter={() => setDisplayInfo(true)}
                                                onMouseLeave={() => setDisplayInfo(false)}
                                                className='relative'
                                            >
                                                <button onClick={handleSubmit} 
                                                    disabled 
                                                    className="bg-[#96d600] opacity-50 cursor-not-allowed rounded-md p-2 pl-4 pr-4">
                                                    Create rule
                                                </button>
                                                {displayInfo &&
                                                    <>
                                                        <motion.div
                                                            className="absolute min-w-64 max-w-64 bg-black text-white text-sm rounded py-1 px-3 left-[-50%] top-[110%]"
                                                        >
                                                            <div className="flex flex-col">
                                                                <span>
                                                                    {selectedTemplateId === null ? "Missing template" : ""}
                                                                </span>
                                                                <span>
                                                                    {selectedDays.length === 0 ? "Missing days" : ""}
                                                                </span>
                                                                <span>
                                                                    {selectedStartTime.includes(null) ? "Missing start time" : ""}
                                                                </span>
                                                                <span>
                                                                    {selectedEndTime.includes(null) ? "Missing end time" : ""}
                                                                </span>
                                                            </div>
                                                        </motion.div>
                                                        <MdArrowDropUp className="absolute top-[85%] left-[50%] translate-x-[-50%]"/>
                                                    </>
                                                }
                                            </div>
                                        }
                                        
                                    </div>
                                </div>
                            </div>
                            <div className="w-[60%] text-lg flex flex-col m-auto">
                                <div className="flex w-full h-[8%] items-center place-items-center">
                                    <span className="font-medium text-3xl pb-1">Template Preview:</span>
                                </div>
                                <div className="aspect-video relative border-4 border-gray-300 rounded-md">
                                    {selectedTemplateId !== null && templates[selectedTemplateId - 1].templateWidgets.map((templateWidget, index) => 
                                    <div key={templateWidget.id} className={`absolute`}
                                        style={{
                                            width: `${templateWidget.width}%`,
                                            height: `${templateWidget.height}%`,
                                            top: `${templateWidget.top}%`,
                                            left: `${templateWidget.leftPosition}%`
                                        }}> 
                                        <div className={`h-full w-full absolute flex flex-col items-center place-content-center border-2 rounded-sm ${selectedColors[index]}`}>
                                            {contentElement(templateWidget)}
                                        </div>
                                    </div>
                                    )}
                                    <AnimatePresence>
                                        {showContentsPortal && <ScheduleContentModal
                                            setShowContentsPortal={setShowContentsPortal}
                                            widgetId={selectedWidgetId}
                                            contents={selectedContent}
                                            setContents={setSelectedContent} />
                                        }
                                    </AnimatePresence>
                                </div>
                            </div>
                        </div>
                    </div>
                </motion.div>
            </motion.div>,
        document.body
    );
}


ScheduleModal.propTypes = {
    showPortal: PropTypes.bool.isRequired,
    setShowPortal: PropTypes.func.isRequired
}

export default ScheduleModal;