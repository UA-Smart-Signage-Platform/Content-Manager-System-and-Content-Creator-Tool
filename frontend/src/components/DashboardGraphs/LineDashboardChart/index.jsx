import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { CustomTooltip } from './customToolTip';


function LineDashboardChart( { data, xLabel, xDataKey, height, title, linkTo, lineDataKey, severity } ) {
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
                            dataKey={xDataKey}
                            fontSize={14} 
                            fontFamily='Lexend' />
                        <YAxis 
                            width={50} 
                            fontSize={16} 
                            fontFamily='Lexend' />
                        <Tooltip content={CustomTooltip}/>
                        <Line type="linear" dataKey={lineDataKey} stroke={"#D12E2E"} strokeWidth={2}/>
                    </LineChart>
                </ResponsiveContainer>
            </div>
            <div className="h-[5%] pl-3 pt-1 text-sm text-textcolor">
                <Link to={linkTo} state={{ severity: severity }}>(Click here for more details)</Link>
            </div>
        </>
    )
}

LineDashboardChart.propTypes = {
    data: PropTypes.array.isRequired,
    xLabel: PropTypes.string.isRequired,
    height: PropTypes.string.isRequired,
    title: PropTypes.node.isRequired,
    linkTo: PropTypes.string.isRequired
}

export default LineDashboardChart