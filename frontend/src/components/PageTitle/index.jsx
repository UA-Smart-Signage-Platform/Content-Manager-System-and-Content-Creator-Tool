import EndTitleHtml from './EndTitleHtml';
import MiddleTitleHtml from './MiddleTitleHtml';
import StartTitleHtml from './StartTitleHtml';
import PropTypes from 'prop-types';

function PageTitle({ startTitle, middleTitle, endTitle, updater, setUpdater, groupId, status }) {
    return (
        <div className="flex flex-col h-full">
            <div id="centerItems" className="items-end h-full w-full flex">
                <div id="startTitle" className="ml-4 w-[20%] flex mb-2">
                    <StartTitleHtml page={startTitle} />
                </div>
                <div id="middleTitle" className="w-[60%] ml-10">
                    <MiddleTitleHtml page={middleTitle}/>
                </div>
                <div id="endTitle" className="flex ml-auto mb-3 mr-1">
                    <EndTitleHtml page={endTitle} updater={updater} setUpdater={setUpdater} />
                </div>
            </div>
            <div id="dividerHr" className="border-[1px] border-secondary flex-col"/>
        </div>
    )
}

PageTitle.propTypes = {
    startTitle: PropTypes.string.isRequired,
    middleTitle: PropTypes.string.isRequired,
    endTitle: PropTypes.string.isRequired,
    updater: PropTypes.bool,
    setUpdater: PropTypes.func
}

export default PageTitle;