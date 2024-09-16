export const priorityDown = (rule, rulesByGroupIdQuery, setChangesMade) => {
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

    updateChanges(rulesByGroupIdQuery, setChangesMade, arr)
};

export const priorityUp = (rule, rulesByGroupIdQuery, setChangesMade) => {
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

    updateChanges(rulesByGroupIdQuery, setChangesMade, arr)
};

const updateChanges = (rulesByGroupIdQuery, setChangesMade, arr) => {
    rulesByGroupIdQuery.data.data = arr;
    setChangesMade(true);
}