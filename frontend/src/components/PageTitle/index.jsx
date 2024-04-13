import EndTitleHtml from './EndTitleHtml';
import MiddleTitleHtml from './MiddleTitleHtml';
import StartTitleHtml from './StartTitleHtml';

function PageTitle({ startTitle, middleTitle, endTitle }) {
    return (
        <div className="flex flex-col h-full">
            <div id="centerItems" className="items-end h-full w-full flex">
                <div id="startTitle" className="ml-4 w-[20%] flex mb-2">
                    <StartTitleHtml page={startTitle} />
                </div>
                <div id="middleTitle" className="w-[60%]">
                    <MiddleTitleHtml page={middleTitle} />
                </div>
                <div id="endTitle" className="flex ml-auto mb-3 mr-1">
                    < EndTitleHtml page={endTitle} />
                </div>
            </div>
            <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
        </div>
    )
}

export default PageTitle;