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
        case ErrorCode.UNAUTHORIZED:
            break;
        case ErrorCode.SERVER_ERROR:
            return pageNotFound(); 
        default:
            break;
    }
}

export default Errors;