import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';

const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload) {
      return (
        <div className="custom-tooltip">
            <p className="label text-textcolor bg-backgroundcolor text-center rounded-md p-2">
                {payload[0].value !== 0 
                ? (
                <>
                    {payload[0].value} monitors offline <br /> during {payload[0].payload.hasOwnProperty("day") ? "day" : "hour"} {label}
                </>
                ) : ( 
                <>
                    No monitors offline <br /> during {payload[0].payload.hasOwnProperty("day") ? "day" : "hour"} {label}
                </>
                )}
            </p>
        </div>
      );
    }
};

CustomTooltip.propTypes = {
    active: PropTypes.bool.isRequired,
    payload: PropTypes.object.isRequired,
    label: PropTypes.object.isRequired
}

function DashboardGraph( { data, xLabel, yLabel, height, title, linkTo } ) {
    return(
        <>
            <div className="h-[10%] flex items-center">
                <span className="flex flex-row text-lg">
                    {title}
                </span>
            </div>
            <div className={`h-[${height}] w-full flex bg-secondaryLight rounded-md`}>
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart margin={{ top: 15, left: 0, right: 15, bottom: 15 }} data={data}>
                        <CartesianGrid strokeDasharray="4 4" />
                        <XAxis label={{ value: xLabel, dy: 18 }} 
                            dataKey={xLabel}
                            fontSize={14} 
                            fontFamily='Lexend' />
                        <YAxis label={{ value: yLabel, angle: -90, dx: -15 }} 
                            width={50} 
                            fontSize={16} 
                            fontFamily='Lexend' />
                        <Tooltip content={<CustomTooltip/>}/>
                        <Line type="linear" dataKey="monitor" stroke={"#D12E2E"} strokeWidth={2}/>
                    </LineChart>
                </ResponsiveContainer>
            </div>
            <div className="h-[5%] pl-3 pt-1 text-sm text-textcolor">
                <Link to={linkTo}>(Click here for more details)</Link>
            </div>
        </>
    )
}

DashboardGraph.propTypes = {
    data: PropTypes.array.isRequired,
    xLabel: PropTypes.string.isRequired,
    yLabel: PropTypes.string.isRequired,
    height: PropTypes.string.isRequired,
    title: PropTypes.node.isRequired,
    linkTo: PropTypes.string.isRequired
}

export default DashboardGraph