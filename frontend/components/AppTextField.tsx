import {TextField, TextFieldProps} from "@material-ui/core";
import React from "react";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
    root: {
        borderRadius: "20px",
    },
    input: {
        padding: "20px 17px 20px",
        fontWeight: "bold",
        fontSize: "0.85em",
    },
    underline: {
        "&&&:before": {
            borderBottom: "none"
        },
        "&&:after": {
            borderBottom: "none"
        }
    }
});

const AppTextField = (props: TextFieldProps) => {
    const classes = useStyles();

    return (
        <TextField
            {...props }
            fullWidth
            variant="filled"
            margin="normal"
            InputProps={{classes}}
        />
    )
}

export default AppTextField;