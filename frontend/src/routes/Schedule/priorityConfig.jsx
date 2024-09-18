export const priorityDown = (rule, rulesByGroupIdQuery, setChangesMade, setPriorityChanged, priorityChanged) => {
    const priority = rule.schedule.priority;

    if (priority === rulesByGroupIdQuery.data.data.length - 1){
        return;
    }

    let arr = rulesByGroupIdQuery.data.data.map((element) => {
        if (element.schedule.priority === priority){
            element.schedule.priority++;
        }
        else if (element.schedule.priority - 1 === priority){
            element.schedule.priority--;
        }
        return element;
    })

    handlePriorityChange(rulesByGroupIdQuery, setChangesMade, setPriorityChanged, priorityChanged, arr)
};

export const priorityUp = (rule, rulesByGroupIdQuery, setChangesMade, setPriorityChanged, priorityChanged) => {
    const priority = rule.schedule.priority;

    if (priority === 0){
        return;
    }

    let arr = rulesByGroupIdQuery.data.data.map((element) => {
        if (element.schedule.priority === priority){
            element.schedule.priority--;
        }
        else if (element.schedule.priority + 1 === priority){
            element.schedule.priority++;
        }
        return element;
    })

    handlePriorityChange(rulesByGroupIdQuery, setChangesMade, setPriorityChanged, priorityChanged, arr);
};

const handlePriorityChange = (rulesByGroupIdQuery, setChangesMade, setPriorityChanged, priorityChanged, arr) => {
    rulesByGroupIdQuery.data.data = arr;
    setPriorityChanged(!priorityChanged);
    setChangesMade(true);
}