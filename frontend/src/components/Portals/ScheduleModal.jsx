import { createPortal } from 'react-dom';
import { MdArrowBack } from "react-icons/md";
import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import templateService from "../../services/templateService";
import activeTemplateService from "../../services/activeTemplateService";
import ScheduleContentModal from './ScheduleContentModal';
import Select from 'react-select'
import DatePicker from 'react-date-picker';
import 'react-date-picker/dist/DatePicker.css';
import 'react-calendar/dist/Calendar.css';

function ScheduleModal( { showPortal, setShowPortal, selectedGroup } ) {
    const [templates, setTemplates] = useState([]);
    const [selectColors,setSelectedColors] = useState([]);

    const [selectedTemplateId, setSelectedTemplateId] = useState(null);
    const [selectedDays, setSelectedDays] = useState([]);
    const [selectedStartTime, setSelectedStartTime] = useState(["00", "00"]);
    const [selectedEndTime, setSelectedEndTime] = useState(["00", "00"]);
    const [selectedStartDate, setSelectedStartDate] = useState(new Date());
    const [selectedEndDate, setSelectedEndDate] = useState(new Date());
    const [selectedContent, setSelectedContent] = useState({});

    const [showContentsPortal, setShowContentsPortal] = useState(false);

    const weekDays = ["RRule.MO", "RRule.TU", "RRule.WE", "RRule.TH", "RRule.FR", "RRule.SA", "RRule.SU"];
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
        const data = {
            template: { id: selectedTemplateId },
            group: { id: selectedGroup.id },
            content: { selectedContent },
            rule: { weekDays : selectedDays,
                    startTime : selectedStartTime,
                    endTime : selectedEndTime,
                    startDate : selectedStartDate,
                    endDate : selectedEndDate}
        }
        console.log(data);
        //activeTemplateService.changeActiveTemplate(data);
    };


    const handleSelectedDays = (event) => {
        const { value } = event.target;
        if (selectedDays.includes(value)) {
            setSelectedDays(selectedDays.filter(day => day !== value));
        } 
        else {
            setSelectedDays([...selectedDays, value]);
        }
    };

    const handleSelectedContent = (event) => {
        if (selectedContent.hasOwnProperty(event.value)) {
            setSelectedContent({...selectedContent, [event.value] : event.label });
        } 
        else {
            setSelectedContent({...selectedContent, [event.value] : event.label });
        }
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
                            <div className="w-[40%] text-xl">
                                <div className="w-full h-full flex flex-col items-center pt-[10%]">
                                    <span className="text-2xl">Creating new rule for <span className="font-medium">{selectedGroup.name}</span></span>
                                    <div className="text-lg flex flex-row h-[15%] w-full justify-evenly pt-6">
                                        <div className="flex pt-5">
                                            <select id="templateSelect" onChange={(e) => setSelectedTemplateId(e.target.value)} className="bg-[#E9E9E9] rounded-md p-2">
                                                <option selected disabled hidden>Template</option>
                                                {templates.length !== 0 && templates.map((template) => 
                                                    <option value={template.id}>{template.name}</option>
                                                )}
                                            </select>
                                        </div>
                                        <div className="flex pt-5">
                                            <select id="templateSelect" className="bg-[#E9E9E9] rounded-md p-2 cursor-pointer">
                                                <option selected disabled hidden>Default rules</option>
                                                <option onClick={handleDisplayAllTime}>Display 24/7</option>
                                                <option onClick={handleDisplayWeeklyFrom8Till23}>Weekly 08:40 - 23:00</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div className="h-[75%] w-full pt-[10%] pb-[4%] pr-[12%] pl-[12%]">
                                        <div className="h-full w-full bg-[#E9E9E9] rounded-md">
                                            <div className="h-[25%] pt-3 w-full flex flex-row">
                                                <div className="h-full w-[50%] flex flex-col items-center justify-center">
                                                    <label className="pb-1">Start Time:</label>
                                                    <div className="flex items-center rounded-md border border-gray-300">
                                                        <select value={selectedStartTime[0]} 
                                                                onChange={(event) => setSelectedStartTime([event.target.value, selectedStartTime[1]])} 
                                                                className="p-2 pr-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeHour.map((hour) => (
                                                                <option value={hour}>{hour}</option>
                                                            ))}
                                                        </select>
                                                        <span className="mx-2">:</span>
                                                        <select value={selectedStartTime[1]} 
                                                                onChange={(event) => setSelectedStartTime([selectedStartTime[0], event.target.value])} 
                                                                className="p-2 pl-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeMinute.map((minute) => (
                                                                <option selected={selectedStartTime[1]} value={minute}>{minute}</option>
                                                            ))}
                                                        </select>
                                                    </div>
                                                </div>
                                                <div className="h-full w-[50%] flex flex-col items-center justify-center">
                                                    <label className="pb-1">End Time:</label>
                                                    <div className="flex items-center rounded-md border border-gray-300">
                                                        <select value={selectedEndTime[0]} 
                                                                onChange={(event) => setSelectedEndTime([event.target.value, selectedEndTime[1]])} 
                                                                className="p-2 pr-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeHour.map((hour) => (
                                                                <option value={hour}>{hour}</option>
                                                            ))}
                                                        </select>
                                                        <span className="mx-2">:</span>
                                                        <select value={selectedEndTime[1]} 
                                                                onChange={(event) => setSelectedEndTime([selectedEndTime[0], event.target.value])} 
                                                                className="p-2 pl-0 appearance-none bg-transparent border-none outline-none">
                                                            <option selected disabled hidden>--</option>
                                                            {timeMinute.map((minute) => (
                                                                <option value={minute}>{minute}</option>
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
                                                                <label>MON</label>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[1])} 
                                                                        value={weekDays[1]} type="checkbox" className="h-5 w-5" />
                                                                <label>TUE</label>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[2])} 
                                                                        value={weekDays[2]} type="checkbox" className="h-5 w-5" />
                                                                <label>WED</label>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[3])} 
                                                                        value={weekDays[3]} type="checkbox" className="h-5 w-5" />
                                                                <label>THU</label>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[4])} 
                                                                        value={weekDays[4]} type="checkbox" className="h-5 w-5" />
                                                                <label>FRI</label>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[5])} 
                                                                        value={weekDays[5]} type="checkbox" className="h-5 w-5" />
                                                                <label>SAT</label>
                                                            </div>
                                                            <div className="flex flex-col items-center">
                                                                <input onChange={handleSelectedDays} 
                                                                        checked={selectedDays.includes(weekDays[6])} 
                                                                        value={weekDays[6]} type="checkbox" className="h-5 w-5" />
                                                                <label>SUN</label>
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
                                                                minDate={new Date()}
                                                                format="dd/MM/y"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="flex text-xl h-[10%] w-full items-center place-content-center ">
                                        <button onClick={handleSubmit} className="bg-[#96d600] rounded-md p-2 pl-4 pr-4">Create rule</button>
                                    </div>
                                </div>
                            </div>
                            <div></div>
                            <div className="w-[60%] text-lg flex flex-col">
                                <div className="flex w-full h-[8%] items-center place-items-center">
                                    <span className="font-medium text-3xl">Template Preview:</span>
                                </div>
                                <div className="w-full h-[92%] relative border-4 border-gray-300 rounded-md">
                                    {selectedTemplateId !== null && templates[selectedTemplateId - 1].templateWidgets.map((templateWidget, index) => 
                                    <div className={`absolute`}
                                        style={{
                                            width: `${templateWidget.width}%`,
                                            height: `${templateWidget.height}%`,
                                            top: `${templateWidget.top}%`,
                                            left: `${templateWidget.leftPosition}%`
                                        }}> 
                                        <div className={`h-full w-full absolute flex flex-col items-center place-content-center border-2 rounded-sm ${selectColors[index]}`}>
                                            {templateWidget.widget.name === "Media" && 
                                                <>
                                                    <span className='z-10'>{templateWidget.widget.name}</span>
                                                    <button onClick={() => setShowContentsPortal(true)} className="bg-[#E9E9E9] border-2 border-black pl-4 pr-4 rounded-lg">
                                                        <span>...</span>
                                                    </button>
                                                </>
                                            }
                                            {templateWidget.widget.name !== "Media" &&
                                                <Select
                                                    placeholder={templateWidget.widget.name + "..."}
                                                    isSearchable="true"
                                                    onChange={(e) => handleSelectedContent(e)}
                                                    options={templateWidget.widget.contents.length !== 0 ? 
                                                                templateWidget.widget.contents[0].options.map(option => ({ value: templateWidget.widget.id, label: option })) 
                                                                : []}
                                                />
                                            }
                                        </div>
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