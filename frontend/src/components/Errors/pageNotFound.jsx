import { Link } from "react-router-dom"

export const pageNotFound = () => {
    return (
    <div className="flex items-center justify-center min-h-screen">
        <div className="relative text-center">
            <h1 className="text-9xl font-extrabold text-gray-800 tracking-widest">
                404
            </h1>
            <div className="bg-primary text-textcolor text-opacity-80 px-4 py-1 text-sm rounded-md rotate-12 absolute -top-4 -right-4 shadow-lg">
                Page Not Found
            </div>
            <p className="text-gray-600 text-xl mt-8">
                Oops! The page you're looking for doesn't exist.
            </p>
            <p className="text-gray-500 text-lg mt-2">
                It might have been moved or deleted.
            </p>
            <Link 
            to="/monitors" 
            className="mt-10 inline-block px-10 py-3 bg-primary text-textcolor rounded-md shadow-md hover:opacity-80 hover:bg-primary-dark hover:shadow-lg transition duration-300"
            >
                Return to Monitors
            </Link>
        </div>
    </div>
    )
}