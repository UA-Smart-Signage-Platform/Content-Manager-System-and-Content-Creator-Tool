import PropTypes from 'prop-types';

export const columns = [
    {
        id: "timestamp",
        name: "Time",
        selector: row => row.timestamp,
        sortable: true
    },
    {
        name: "Module",
        selector: row => row.module,
        sortable: true
    },
    {
        name: "Operation",
        selector: row => row.operation,
        sortable: true
    },
    {
        name: "User",
        selector: row => row.user,
        sortable: true
    },
    {
        name: "Description",
        selector: row => row.description
    },
    {
        name: "Severity",
        selector: row => row.severity,
        omit: true
    }
];

export const ExpandedComponent = ({ data }) => {
    const getSeverityColor = (severity) => {
      switch (severity) {
        case "INFO":
          return "bg-[#A1A1A1] bg-opacity-70 rounded-none";
        case "WARNING":
          return "bg-[#DC8331] bg-opacity-70 rounded-none";
        case "ERROR":
          return "bg[#D12E2E] bg-opacity-70 rounded-none";
        default:
          return "bg-gray-100 rounded-none";
      }
    };
  
    return (
      <pre className={`${getSeverityColor(data.severity)} p-4 rounded-md`}>
        {JSON.stringify(data, null, 2)}
      </pre>
    );
  };


CustomLabel.propTypes = {
  data: PropTypes.object.isRequired
}