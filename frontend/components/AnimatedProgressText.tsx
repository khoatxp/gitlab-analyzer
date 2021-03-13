import React from "react";
import ChildrenProps from "../interfaces/ChildrenProps";
import { makeStyles } from "@material-ui/core/styles";
import {Typography} from "@material-ui/core";

const useStyles = makeStyles({
    progress_done: {
        display: 'none',
    },
    progress_dots: {
        margin: 0,
        '&::after': {
            content: '"......."', // Need double quotes to display
            animation: '$dots 1s steps(5, end) infinite',
        }
    },
    '@keyframes dots': {
        '0%': {
            color: 'rgba(0, 0, 0, 0)',
            textShadow: '.25em 0 0 rgba(0, 0, 0, 0), .5em 0 0 rgba(0, 0, 0, 0)',
        },
        '40%': {
            color: 'white',
            textShadow: '.25em 0 0 rgba(0, 0, 0, 0), .5em 0 0 rgba(0, 0, 0, 0)',
        },
    },
});

type AnimatedProgressTextProps = {progress: number} & ChildrenProps
const AnimatedProgressText = ({children, progress}: AnimatedProgressTextProps) => {
    const classes = useStyles();
    return (
        <Typography variant="h6" className={`${classes.progress_dots} ${progress === 100 && classes.progress_done}`}>
            {children}
        </Typography>
    )
}

export default AnimatedProgressText;