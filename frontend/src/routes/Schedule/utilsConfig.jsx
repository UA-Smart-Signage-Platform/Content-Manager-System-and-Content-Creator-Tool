export const warningPopUp = (showGroupNeeded) => {
    if (showGroupNeeded){
        return(
            <div className="absolute text-md text-red h-full top-10 right-1">
                You must select a group
            </div>
        )
    }
}