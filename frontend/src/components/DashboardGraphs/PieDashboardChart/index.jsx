import PropTypes from 'prop-types';
import { Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts';
import { CustomTooltip } from './customToolTip';
import { CustomLabel } from './customLabel';

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
                    {newData.length > 0 ? (
                        <PieChart >
                            <Pie data={newData} dataKey="count" nameKey="operationSource" cx="50%" cy="50%" outerRadius={90} fill="#a7a8a9" labelLine={false} label={CustomLabel}/>
                            <Tooltip content={CustomTooltip}/>
                        </PieChart>
                    ) :
                        <div className="w-full h-full flex place-content-center items-center text-textcolor text-xl">
                            No data
                        </div>
                    }

                </ResponsiveContainer>
            </div>
        </>
    )
}

PieDashboardChart.propTypes = {
    height: PropTypes.string.isRequired,
    title: PropTypes.node.isRequired,
    data: PropTypes.array.isRequired
}

export default PieDashboardChart