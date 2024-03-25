import { motion } from "framer-motion";

function MemorySvg({className,usado,max,full}){

    const draw = {
        hidden: { pathLength: 0, opacity: 0 },
        visible: () => {
          const delay =  0;
          return {
            pathLength: 1,
            opacity: 1,
            transition: {
              pathLength: { delay, type: "spring", duration: 2, bounce: 0 },
              opacity: { delay, duration: 0.01 }
            },
          };
        }
      };
      const draw2 = {
        hidden: { pathLength: 0, opacity: 0 },
        visible: () => {
          const delay =  0.5;
          return {
            pathLength: full,
            opacity: 1,
            transition: {
              pathLength: { delay, type: "spring", duration: 2, bounce: 0 },
              opacity: { delay, duration: 0.01 }
            },
          };
        }
      };

    return(
    <div className={`relative text-center ${className}`}>
        <div className="flex items-center rotate-[-90deg] h-full">
          <motion.svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 100 100"
            width="100%"
            height="100%"
            initial="hidden"
            animate="visible"
          >
            <motion.circle
              cx="50"
              cy="50"
              r="40"
              fill="none" // No fill
              stroke="#57E574" // Border color
              strokeWidth="1.5" // Border width
              variants={draw}
            />
            <motion.circle style={{strokeLinecap:"round"}}
              cx="50"
              cy="50"
              r="40"
              fill="none" // No fill
              stroke="#97D700" // Border color
              strokeWidth="5" // Border width
              variants={draw2}
            />
            <motion.text textAnchor="middle" x="50" y="-45" fontSize="13" transform="rotate(90)"
                initial={{opacity:0}}
                animate={{opacity:1}}
                transition={{duration:1.5}}

            >{usado}/{max}GB</motion.text>
          </motion.svg>
        </div>
    </div>
    )
}

export default MemorySvg;