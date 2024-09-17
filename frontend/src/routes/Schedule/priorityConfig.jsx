export const priorityDown = (rule, rulesByGroupIdQuery, setChangesMade, setPriorityChanged) => {
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

    handlePriorityChange(rulesByGroupIdQuery, setChangesMade, setPriorityChanged, arr)
};

export const priorityUp = (rule, rulesByGroupIdQuery, setChangesMade, setPriorityChanged) => {
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

    handlePriorityChange(rulesByGroupIdQuery, setChangesMade, setPriorityChanged, arr);
};

const handlePriorityChange = (rulesByGroupIdQuery, setChangesMade, setPriorityChanged, arr) => {
    rulesByGroupIdQuery.data.data = arr;
    setPriorityChanged(prevState => !prevState);
    setChangesMade(true);
}