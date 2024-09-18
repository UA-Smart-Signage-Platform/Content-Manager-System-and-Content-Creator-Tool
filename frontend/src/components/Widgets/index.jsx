import { mediaWidget } from "./mediaWidgetConfig";
import { optionWidget } from "./optionWidgetConfig";
import { WidgetType } from "./widgetUtils";
import PropTypes from 'prop-types';

function Widgets( { widgetType, templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent } ) {
    switch (widgetType) {
        case WidgetType.MEDIA:
            return mediaWidget(templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent);
        case WidgetType.OPTION:
            return optionWidget(templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent);
        case WidgetType.TEXT:
            break;
        default:
            return <span>{templateWidget.widget.name}</span>;
    }
}

Widgets.propTypes = {
    widgetType: PropTypes.string.isRequired,
    templateWidget: PropTypes.object.isRequired,
    setShowContentsPortal:PropTypes.func,
    setSelectedWidgetId: PropTypes.func,
    selectedContent: PropTypes.array
}

export default Widgets;