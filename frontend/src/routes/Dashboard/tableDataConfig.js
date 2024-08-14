export const paginationComponentOptions = {
    noRowsPerPage: true,
    selectAllRowsItem: false,
};

export const columns = [
    {
        name: (                
            <div className="flex flex-row">
                 Name
            </div>
        ),
        selector: row => <div data-tag="allowRowEvents" className='flex items-center'>
                            <MdRemoveRedEye data-tag="allowRowEvents" className="border h-5 w-5 rounded border-black mr-3" /> 
                            <span data-tag="allowRowEvents">{row.name}</span> 
                        </div>,
        sortable: true,
    },
    {
        name: (                
            <div className="flex flex-row">
                Group
            </div>
        ),
        selector: row => !row.group.madeForMonitor ? row.group.name : "-----",
        sortable: true,
        width: "220px"
    },
    {
        name: (                
            <div className="flex flex-row">
                 Warns (5 days)
            </div>
        ),
        selector: row => row.warnings,
        width: "175px"
    },
    {
        name: (                
            <div className="flex flex-row">
                 Online for
            </div>
        ),
        selector: row => row.online,
        width: "160px"
    }   
];