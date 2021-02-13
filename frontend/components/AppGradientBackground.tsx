import {Box} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import React from "react";
import ChildrenProps from "../interfaces/ChildrenProps";

const useStyles = makeStyles((theme) => ({
    backgroundGradient: {
        background: `radial-gradient(113% 113% at 50% 45%, #FFFFFF 0%, ${theme.palette.primary.main} 65%)`,
    },
    card: {
        background: "white",
    },
}));

const AppGradientBackground = ({children}: ChildrenProps) => {
    const classes = useStyles();
    return (
        <Box
            className={classes.backgroundGradient}
            height="100vh"
            width="100vw"
            display="flex"
            justifyContent="center"
            alignItems="center"
        >
            {children}
        </Box>
    );
};

export default AppGradientBackground;
