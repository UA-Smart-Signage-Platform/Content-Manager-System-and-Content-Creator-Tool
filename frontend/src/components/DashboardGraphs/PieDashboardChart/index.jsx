import PropTypes from 'prop-types';
import { Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts';
import { CustomTooltip } from './customToolTip';

function PieDashboardChart( { height, title, data } ) {
  const newData = [];

  for(const [key, value] of Object.entries(data)){
    newData.push({operationSource: key, count: value})
  }

    return(
        <>
            <div className="h-[10%] flex items-center place-content-center">
                <span className="flex flex-row text-lg">
                    {title}
                </span>
            </div>
            <div className={`h-[${height}] w-full flex`}>
                <ResponsiveContainer width="100%" height="100%">
                    <PieChart >
                        <Pie data={newData} dataKey="count" nameKey="operationSource" cx="50%" cy="50%" outerRadius={90} />
                        <Tooltip content={CustomTooltip}/>
                    </PieChart>
                </ResponsiveContainer>
            </div>
        </>
    )
}

PieDashboardChart.propTypes = {
    height: PropTypes.string.isRequired,
    title: PropTypes.node.isRequired
}

export default PieDashboardChart