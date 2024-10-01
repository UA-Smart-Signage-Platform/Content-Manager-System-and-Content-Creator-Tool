import PropTypes from 'prop-types';
import { Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts';
import { CustomTooltip } from './customToolTip';

const data01 = [
    {
      "name": "Group A",
      "value": 400
    },
    {
      "name": "Group B",
      "value": 300
    },
    {
      "name": "Group C",
      "value": 300
    },
    {
      "name": "Group D",
      "value": 200
    },
    {
      "name": "Group E",
      "value": 278
    },
    {
      "name": "Group F",
      "value": 189
    }
  ];

function PieDashboardChart( { height, title } ) {
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
                        <Pie data={data01} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={90} />
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