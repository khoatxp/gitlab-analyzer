import {Button, PropTypes} from "@material-ui/core";
import React, {MouseEventHandler} from "react";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
    button: {
        borderRadius: "20px",
        padding: "12px 30px",
        margin: "10px",
    }
})

type AppButtonProps = {
    color?: PropTypes.Color,
    onClick: MouseEventHandler,
    children: React.ReactNode
}

const AppButton = ({color, onClick, children}: AppButtonProps) => {
    const classes = useStyles();

    return (
        <Button
            className={classes.button}
            variant="contained"
            color={color}
            onClick={onClick}
            disableElevation
        >
            {children}
        </Button>
    )
}

export default AppButton;