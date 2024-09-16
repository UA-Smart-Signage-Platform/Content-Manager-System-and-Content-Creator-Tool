import { MdOutlineFolder, MdOutlineInsertPhoto, MdLocalMovies } from "react-icons/md";

export const conditionalRowStyles = [
    {
      when: row => row.selected,
      style: {
        backgroundColor: '#a7a8a9'
      }
    }
];

export const columns = [
    {
        selector: row => headNameStyle(row),
    },
    {
        id: 'isFolder',
        selector: row => row.type,
        omit: true,
    },
];

export const customStyles = {
    head: {
        style: {
            fontSize: '20px',
        },
    },
    rows: {
        style: {
            fontSize: '16px',
        },
    }
};

const headNameStyle = (row) => {
    return(
        <div data-tag="allowRowEvents" className="flex flex-row items-center">
            {getFileIcon(row.type)}
            <span data-tag="allowRowEvents" className="ml-2">{row.name}</span>
        </div>
    )
};

const getFileIcon = (type) => {
    switch (type) {
        case "directory":
            return <MdOutlineFolder data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
        case "image/png":
            return <MdOutlineInsertPhoto data-tag="allowRowEvents" className="h-7 w-7 mr-2"/>;
        case "video/mp4":
            return <MdLocalMovies data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
        default:
            break;
    }
};