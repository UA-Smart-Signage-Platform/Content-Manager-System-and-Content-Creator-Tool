export const mediaWidget = (templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent) => {
    return (
        <>
            <span className='z-10'>{templateWidget.widget.name}</span>
            <button onClick={() => { setSelectedWidgetId(`${templateWidget.id}`); setShowContentsPortal(true) }} 
                    className="bg-[#E9E9E9] border-2 border-black pl-4 pr-4 rounded-lg">
                <span>...</span>
            </button>
            {selectedContent[templateWidget.id] !== undefined && (selectedContent[templateWidget.id]).name}
        </>
    )
}