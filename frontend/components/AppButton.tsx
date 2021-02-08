import {Button, ButtonProps} from "@material-ui/core";
import React from "react";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
    button: {
        borderRadius: "20px",
        padding: "12px 30px",
        margin: "10px",
    }
})

const AppButton = (props: ButtonProps) => {
    const classes = useStyles();

    return (
        <Button
            {...props}
            className={classes.button}
            variant="contained"
            disableElevation
        />
    )
}

export default AppButton;