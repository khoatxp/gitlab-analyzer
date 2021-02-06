import {TextField} from "@material-ui/core";
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

type TextFieldProps = {
    placeholder: string,
    value: string,
    setValue: Function
}

const AppTextField = ({placeholder, value, setValue}: TextFieldProps) => {
    const classes = useStyles();

    return (
        <TextField
            fullWidth
            placeholder={placeholder.toUpperCase()}
            variant="filled"
            margin="normal"
            value={value}
            InputProps={{classes}}
            onChange={(e) => setValue(e.target.value)}
        />
    )
}

export default AppTextField;