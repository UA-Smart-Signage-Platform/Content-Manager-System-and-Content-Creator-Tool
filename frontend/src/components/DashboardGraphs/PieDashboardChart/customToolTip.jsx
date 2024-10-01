import { IoMdInformationCircle } from 'react-icons/io';

export const CustomTooltip = ({ active, payload }) => {
    if (active && payload) {
        return (
            <div className="flex flex-col items-center bg-gradient-to-r from-purple-400 to-blue-500 text-white border border-gray-300 p-4 rounded-lg shadow-lg">
                <div className="flex items-center mb-2">
                    <IoMdInformationCircle className="mr-2" />
                    <span className="font-semibold text-lg">
                        {payload[0].name}
                    </span>
                </div>
                <p className="text-sm">{`Value: ${payload[0].value}`}</p>
            </div>
        );
    }
}