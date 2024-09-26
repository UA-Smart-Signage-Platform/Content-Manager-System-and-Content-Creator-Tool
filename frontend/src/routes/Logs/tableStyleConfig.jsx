export const tableStyle = {
    head: {
        style: {
            fontWeight: 500,
        },
    },
    headRow: {
        style: {
            fontSize: '20px',
            minHeight: '32px',
            backgroundColor: "#C7C7C7",
        },
    },
    headCells: {
        style: {
            paddingLeft: '16px',
        },
    },
    rows: {
        style: {
            fontSize: '16px',
            minHeight: '32px',
            backgroundColor: "#E9E9E9",
            '&:not(:last-of-type)': {
                borderBottomStyle: 'solid',
                borderBottomWidth: '1px',
                borderBottomColor: "#FFFFFF",
            },
        },
    },
    cells: {
        style: {
            paddingLeft: '10px',
            paddingRight: '0px',
            wordBreak: 'break-word',
        }
    }
};

export const conditionalRowStyles = [
	{
		when: row => row.severity === "INFO",
		style: {
			backgroundColor: "#A1A1A1",
		},
	},
	{
		when: row => row.severity === "WARNING",
		style: {
			backgroundColor: "#DC8331",
		},
	},
	{
		when: row => row.severity === "ERROR",
		style: {
			backgroundColor: "#D12E2E",
		},
	},
];