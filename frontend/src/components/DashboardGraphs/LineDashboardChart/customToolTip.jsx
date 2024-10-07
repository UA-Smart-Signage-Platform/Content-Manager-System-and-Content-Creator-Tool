import PropTypes from 'prop-types';

export const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload) {
        const value = payload[0].value;
        const isLog = payload[0].name.includes("numberLogs");
        const timePeriod = payload[0].payload.hasOwnProperty("day") ? "day" : "hour";

        return (
            <div className="custom-tooltip">
                <p className="label text-textcolor bg-backgroundcolor text-center rounded-md p-2">
                    {value !== 0 ? `${value}` : "No"} {isLog ? "logs" : "monitors offline"} <br /> during {timePeriod} {label}
                </p>
            </div>
        );
    }
};

CustomTooltip.propTypes = {
    active: PropTypes.bool.isRequired,
    payload: PropTypes.array.isRequired,
    label: PropTypes.string
}