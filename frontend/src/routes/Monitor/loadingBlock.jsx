export const loadingBlock = () => {
    return (
        <div className="flex items-center justify-center h-screen">
            <div className="text-center">
                <svg className="w-16 h-16 mx-auto mb-4 animate-spin text-primary" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 016-7.75V4a10 10 0 000 20v-1.25A8 8 0 014 12z" />
                </svg>
                <p className="text-lg text-gray-700">
                    Loading, please wait...
                </p>
            </div>
        </div>
    )
}