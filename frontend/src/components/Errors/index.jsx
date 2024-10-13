import { ErrorCode } from "./errorUtils";
import { pageNotFound } from "./pageNotFound";

function Errors( { errorCode } ) {
    switch (errorCode) {
        case ErrorCode.BAD_REQUEST:
            break;
        case ErrorCode.UNAUTHORIZED:
            break;
        case ErrorCode.FORBIDDEN:
            break;
        case ErrorCode.PAGE_NOT_FOUND:
            return pageNotFound(); 
        case ErrorCode.SERVER_ERROR:
            break;
        default:
            break;
    }
}

export default Errors;