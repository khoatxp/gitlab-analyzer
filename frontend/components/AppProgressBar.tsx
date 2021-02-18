import {createStyles, Theme, withStyles} from "@material-ui/core/styles";
import LinearProgress from "@material-ui/core/LinearProgress";

const AppProgressBar = withStyles((theme: Theme) =>
    createStyles({
        root: {
            height: 50,
            borderRadius: 50,
        },
        colorPrimary: {
            backgroundColor: theme.palette.grey[theme.palette.type === 'light' ? 200 : 700],
        },
        bar: {
            borderRadius: 25,
            backgroundColor: 'secondary',
        },
    }),
)(LinearProgress);

export default AppProgressBar;