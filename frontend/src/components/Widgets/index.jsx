import { mediaWidget } from "./mediaWidgetConfig";
import { optionWidget } from "./optionWidgetConfig";
import { WidgetType } from "./widgetUtils";

function Widgets( { widgetType, templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent } ) {
    switch (widgetType) {
        case WidgetType.MEDIA:
            return mediaWidget(templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent);
        case WidgetType.OPTION:
            return optionWidget(templateWidget);
        case WidgetType.TEXT:
            break;
        default:
            return <span>{templateWidget.widget.name}</span>;
    }
}

export default Widgets;