import { motion } from 'framer-motion';

const breadCrumbs = ({ path, stack, setCurrentFolder, navigate, setPreview, setFile }) => {

    const breadCrumbsNavigate = (index, arraySize) => {
        let newpath = "home/" + path.split("/").slice(2, index + 2).join("/");
        let toPop = arraySize - index;
        for (let i = 0; i < toPop; i++) {
            stack.pop();
        }

        setCurrentFolder(stack.length > 0 ? stack[stack.length - 1] : null);
        setPreview(null);
        setFile(null);
        navigate(newpath);
    };

    return (
        <div className="flex flex-row">
            {path.split("/").slice(1).map((folder, index, array) =>
                <div key={folder + "_breadcrumb"}>
                    <motion.button key={folder} className={(index + 1 !== array.length ? `text-secondary hover:bg-secondaryMedium rounded-lg px-1` : `text-black`)} onClick={() => breadCrumbsNavigate(index, array.length - 1)}
                        whileHover={{ scale: 1.1 }}>
                        {folder !== "uploads" ? folder : "home"}
                    </motion.button>
                    {(array.length > index + 1) && <span className="pr-2 pl-2 text-secondary">&gt;</span>}
                </div>
            )}
        </div>
    );
};

export default breadCrumbs;
