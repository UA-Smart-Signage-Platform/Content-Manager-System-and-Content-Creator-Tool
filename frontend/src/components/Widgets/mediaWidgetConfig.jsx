export const mediaWidget = (templateWidget, setShowContentsPortal, setSelectedWidgetId, selectedContent) => {
    return (
        <>
            <span className='z-10'>{templateWidget.widget.name}</span>
            <button onClick={() => { setSelectedWidgetId(`${templateWidget.id}`); setShowContentsPortal(true) }} 
                    className="bg-gradient-to-r from-blue-400 to-purple-500 text-white px-6 py-1 rounded-full border-none shadow-lg hover:from-blue-500 hover:to-purple-600 transition-all duration-300 ease-in-out transform hover:scale-105">
                <span className="text-lg font-semibold">...</span>
            </button>
            {selectedContent[templateWidget.id] !== undefined && (selectedContent[templateWidget.id]).name}
        </>
    )
}