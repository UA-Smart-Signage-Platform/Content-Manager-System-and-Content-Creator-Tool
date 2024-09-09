import { ErrorCode } from "./errorUtils";
import { pageNotFound } from "./pageNotFound";

function Errors( { errorCode } ) {
    switch (errorCode) {
        case ErrorCode.PAGE_NOT_FOUND:
            return pageNotFound(); 
        default:
            break;
    }
}

export default Errors;