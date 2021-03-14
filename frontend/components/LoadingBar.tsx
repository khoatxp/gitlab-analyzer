import {LinearProgress} from "@material-ui/core";
import React from "react";
import AnimatedProgressText from "./AnimatedProgressText";

type LoadingBarProps = { itemBeingLoaded: string };

const LoadingBar = ({itemBeingLoaded}: LoadingBarProps) => {
    return (
        <div>
            <AnimatedProgressText progress={0}>
                Loading {itemBeingLoaded}
            </AnimatedProgressText>
            <LinearProgress/>
        </div>
    )
};

export default LoadingBar;
