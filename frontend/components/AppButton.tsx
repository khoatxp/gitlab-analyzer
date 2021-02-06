import {Button, PropTypes} from "@material-ui/core";
import React from "react";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
    button: {
        borderRadius: "25px",
        padding: "15px 30px",
        margin: "10px",
    }
})

type AppButtonProps = { color?: PropTypes.Color, children: React.ReactNode }
const AppButton = ({color, children}: AppButtonProps) => {
    const classes = useStyles();

    return (
        <Button
            className={classes.button}
            variant="contained"
            color={color}
            disableElevation
        >
            {children}
        </Button>
    )
}

export default AppButton;