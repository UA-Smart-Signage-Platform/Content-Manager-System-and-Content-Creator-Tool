import PropTypes from 'prop-types';
import { IoMdInformationCircle } from 'react-icons/io';

export const CustomTooltip = ({ active, payload }) => {
    if (active && payload) {
        return (
            <div className="flex flex-col items-center bg-secondaryLight text-textcolor border border-gray-300 p-4 rounded-lg shadow-lg">
                <div className="flex items-center mb-2">
                    <IoMdInformationCircle className="mr-2 text-textcolorNotSelected" />
                    <span className="text-lg">
                        {payload[0].name}
                    </span>
                </div>
                <p className="text-sm">{`Occurences: ${payload[0].value}`}</p>
            </div>
        );
    }
}

CustomTooltip.propTypes = {
    active: PropTypes.bool.isRequired,
    payload: PropTypes.array.isRequired
}