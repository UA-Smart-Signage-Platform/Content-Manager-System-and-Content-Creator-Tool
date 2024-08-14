export const tableStyle = {
    head: {
        style: {
            fontWeight: 400,
        },
    },
    headRow: {
        style: {
            fontSize: '16px',
            minHeight: '32px',
            backgroundColor: "#C1C1C1",
            borderradius: '20px',
            borderBottomWidth: '0px'
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
                borderBottomColor: "#A7A8AA",
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