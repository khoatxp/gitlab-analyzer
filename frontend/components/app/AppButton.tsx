import {Button, ButtonProps} from "@material-ui/core";
import React from "react";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
    button: {
        borderRadius: "15px",
        margin: "10px",
    }
})

const AppButton = React.forwardRef((props: ButtonProps, ref) => {
    const classes = useStyles();

    return (
        <Button
            className={classes.button}
            variant="contained"
            size="large"
            disableElevation
            {...props}
        />
    )
});

export default AppButton;