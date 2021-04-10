import {withStyles} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";

export const MenuButton = withStyles({
    root: {
        textTransform: 'none',
        fontSize: '13px',
        padding: '20px',
        border: '1px solid white',
        color: 'black',
        lineHeight: 0.8,
        backgroundColor: 'white',
        borderRadius: '999px',
        margin: '20px 0',
        width: '80%',
        '&:hover': {
            backgroundColor: '#8FC6F3',
            borderColor: '#8FC6F3',
        },
        '&:active': {
            backgroundColor: 'primary',
            borderColor: '#005cbf',
        },
        '&:focus': {
            boxShadow: '0 0 0 0.2rem rgba(0,123,255,.5)',
            color: '#005cbf',
        },
        '&.selected': {
            boxShadow: '0 0 0 0.2rem rgba(0,123,255,.5)',
            color: '#005cbf',
        }
    },
})(Button);