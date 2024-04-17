import { useEffect, useState } from "react";
import { PageTitle } from "../../components";
import monitorsGroupService from "../../services/monitorsGroupService";
import templateService from "../../services/templateService";
import activeTemplateService from "../../services/activeTemplateService";

function Schedule(){
    const [groups, setGroups] = useState([]);
    const [templates, setTemplates] = useState([]);
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [selectedTemplateId, setSelectedTemplateId] = useState(null);

    useEffect(() => {
        monitorsGroupService.getGroups().then((response) => {
            setGroups(response.data);
        })

        templateService.getTemplates().then((response) => {
            setTemplates(response.data);
        })
    }, []);

    const handleSubmit = ()=>{
        const data = {
            template: { id: selectedTemplateId },
            group: { id: selectedGroupId },
            content: { 1: "leci.mp4", 2: "events.png"},
        }

        activeTemplateService.changeActiveTemplate(data);
    }


    return(
        <div className="h-full flex flex-col">
            <div id="title" className="pt-4 h-[8%]">
                <PageTitle startTitle={"schedule"} 
                            middleTitle={"dashboard"}
                            endTitle={"dashboard"}/>
            </div>
            <div id="divider" className="flex flex-col h-[92%] mr-3 ml-3 ">
                <div className="h-[40%] w-[60%] flex place-content-center items-center mr-auto ml-auto mt-10">
                    <div className="flex flex-col mr-[10%] text-xl">
                        <label for="groupSelect">Select a group:</label>
                        <select id="groupSelect" onChange={(e) => setSelectedGroupId(e.target.value)} className="bg-zinc-300 rounded-md p-2 cursor-pointer">
                            <option selected disabled hidden>Choose here</option>
                            {groups.length !== 0 && groups.map((group) => 
                                <option value={group.id}>{group.name}</option>
                            )}
                        </select>
                    </div>
                    <div className="flex flex-col text-xl">
                        <label for="templateSelect">Select a template:</label>
                        <select id="templateSelect" onChange={(e) => setSelectedTemplateId(e.target.value)} className="bg-zinc-300 rounded-md p-2 cursor-pointer">
                            <option selected disabled hidden>Choose here</option>
                            {templates.length !== 0 && templates.map((template) => 
                                <option value={template.id}>{template.name}</option>
                            )}
                        </select>
                    </div>
                </div>
                <div className="flex place-self-end mr-auto ml-auto mb-10 text-xl">
                    <button onClick={handleSubmit} className="bg-blue-300 rounded-md p-3">Submit</button>
                </div>
            </div>
        </div>
    )
}

export default Schedule;