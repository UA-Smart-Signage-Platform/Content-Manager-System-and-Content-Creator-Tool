import { useEffect, useState } from "react";
import { PageTitle } from "../../components";
import monitorsGroupService from "../../services/monitorsGroupService";

function Schedule(){
    const [groups, setGroups] = useState([]);
    const [templates, setTemplates] = useState([]);

    useEffect(() => {
        monitorsGroupService.getGroups().then((response) => {
            setGroups(response.data);
        })
    }, []);

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
                        <select id="groupSelect" className="bg-zinc-300 rounded-md p-2 cursor-pointer">
                            {groups.length !== 0 && groups.map((group) => 
                            <option value="group1">{group.name}</option>
                            )}
                        </select>
                    </div>
                    <div className="flex flex-col text-xl">
                        <label for="templateSelect">Select a template:</label>
                        <select id="templateSelect" className="bg-zinc-300 rounded-md p-2 cursor-pointer">
                            {groups.length !== 0 && groups.map((group) => 
                            <option value="group1">{group.name}</option>
                            )}
                        </select>
                    </div>
                </div>
                <div className="flex place-self-end mr-auto ml-auto mb-10 text-xl">
                    <button className="bg-blue-300 rounded-md p-3">Submit</button>
                </div>
            </div>
        </div>
    )
}

export default Schedule;