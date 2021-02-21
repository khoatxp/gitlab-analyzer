import React from "react";

import { makeStyles, withStyles, Theme, createStyles } from '@material-ui/core/styles';
import { green, purple } from '@material-ui/core/colors';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox, { CheckboxProps } from '@material-ui/core/Checkbox';
import Avatar from '@material-ui/core/Avatar';
import SingleBarGraph from "./SingleBarGraph";

const GreenCheckbox = withStyles({
    root: {
        color: green[400],
        '&$checked': {
            color: green[600],
        },
    },
    checked: {},
})((props: CheckboxProps) => <Checkbox color="default" {...props} />);

const PurpleCheckbox = withStyles({
    root: {
        color: purple[400],
        '&$checked': {
            color: purple[600],
        },
    },
    checked: {},
})((props: CheckboxProps) => <Checkbox color="default" {...props} />);

// TODO: add real data based on gitlab API request and analysis for the
//       repo name, commit score, merge request score, repo avatar
// TODO: add graph tag where placeholder is

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        container1: {
            display: 'flex',
            width: '100%',
        },
    container2: {
        display: 'flex',
        justifyContent: 'flex-start',
        width: '70%',
    },
    outerContainer: {
        flexDirection: 'column',
        width: '100%',
    },
    textContainer1: {
        flexDirection: 'column',
        margin: '0px 0px 0px 20px',
        padding: '0px',
    },
    textContainer2: {
        flexDirection: 'column',

        width:'30%',
    },
    repoNameText: {
        fontSize: '2em',
        margin: '16px 0px',
    },
    smallTextColor: {
        color: 'grey',
        margin: '0px 0px',
    },
    mrScoreText: {
        fontSize:'1.2em',
        margin: '16px 0px',
        textAlign: 'right'
    },
    commitScoreText: {
        fontSize:'1.2em',
        margin: '0px 0px',
        textAlign: 'right'
    },
    container3: {
        display: 'flex',
        justifyContent: 'space-around',
        margin: '24px 0px',
    },
    graphContainer: {
        display: 'flex',
        justifyContent: 'space-around',
        flexDirection: 'column',
    },
    avatarSize: {
        width: theme.spacing(15),
        height: theme.spacing(15),
    },
  }),
);

const CodeAnalysis = () => {
    const classes = useStyles();
    const [state, setState] = React.useState({
        checkedA: true,
        checkedB: true,
    });

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setState({ ...state, [event.target.name]: event.target.checked });
    };

    return (
    <>
        <div className={classes.outerContainer}>
            <div className={classes.container1}>
                <div className={classes.container2}>
                    <Avatar className={classes.avatarSize} variant='square'>R</Avatar>
                    <div className={classes.textContainer1}>
                        <h1 className={classes.repoNameText}>Repo Name</h1>
                        <p className={classes.smallTextColor}>- 110 Commits - 11 Merge Request -</p>
                    </div>
                </div>
                <div className={classes.textContainer2}>
                    <p className={classes.mrScoreText}>Merge Request Score: 300</p>
                    <p className={classes.commitScoreText}>Commit Score: 120</p>
                </div>
            </div>
            <div className={classes.container3}>
                <div className={classes.graphContainer}>
                    <p> (graph placeholder)            </p>
                </div>
                <FormGroup>
                    <FormControlLabel
                        control={<GreenCheckbox checked={state.checkedG} onChange={handleChange} name="checkedA" />}
                        label="Commits"
                    />
                    <FormControlLabel
                        control={<PurpleCheckbox checked={state.checkedG} onChange={handleChange} name="checkedB" />}
                        label="Merge Requests"
                    />
                </FormGroup>
            </div>
            <div className={classes.container3}>
                <div className={classes.graphContainer}>
                    <p> (graph placeholder)            </p>
                </div>
                <FormGroup>
                    <FormControlLabel
                        control={<GreenCheckbox checked={state.checkedG} onChange={handleChange} name="checkedA" />}
                        label="Commits"
                    />
                    <FormControlLabel
                        control={<PurpleCheckbox checked={state.checkedG} onChange={handleChange} name="checkedB" />}
                        label="Merge Requests"
                    />
                </FormGroup>
            </div>
        </div>
    </>
    );
};

export default CodeAnalysis;