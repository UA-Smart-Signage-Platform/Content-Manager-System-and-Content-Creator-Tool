import { MdOutlineFolder, MdOutlineInsertPhoto, MdLocalMovies } from "react-icons/md";

export const getFileIcon = (type) => {
    switch (type) {
        case "directory":
            return <MdOutlineFolder data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
        case "image/":
            return <MdOutlineInsertPhoto data-tag="allowRowEvents" className="h-7 w-7 mr-2"/>;
        case "video/mp4":
            return <MdLocalMovies data-tag="allowRowEvents" className="h-6 w-6 mr-2"/>;
        default:
            break;
    }
};

// code taken from https://stackoverflow.com/questions/15900485/correct-way-to-convert-size-in-bytes-to-kb-mb-gb-in-javascript
export const formatBytes = (bytes, decimals = 2) => {
    if (!+bytes) return '0 Bytes'

    const k = 1024
    const dm = decimals < 0 ? 0 : decimals
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']

    const i = Math.floor(Math.log(bytes) / Math.log(k))

    return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`
};
