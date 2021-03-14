import React, {useEffect} from "react";
import { makeStyles, withStyles, Theme, createStyles } from '@material-ui/core/styles';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox, { CheckboxProps } from '@material-ui/core/Checkbox';
import Avatar from '@material-ui/core/Avatar';
import axios, {AxiosResponse} from "axios";
import {useRouter} from "next/router";
import {useSnackbar} from "notistack";
import {AuthContext} from "./AuthContext";

import CommitsCountGraph from "../components/graphs/CommitsCountGraph";
import CommitsScoreGraph from "../components/graphs/CommitsScoreGraph";
import MergeRequestsCountGraph from "../components/graphs/MergeRequestsCountGraph";
import MergeRequestsScoreGraph from "../components/graphs/MergeRequestsScoreGraph";
import TotalCountGraph from "../components/graphs/TotalCountGraph";
import TotalScoreGraph from "../components/graphs/TotalScoreGraph";
import EmptyGraph from "../components/graphs/EmptyGraph";

const GreenCheckbox = withStyles({
    root: {
        color: "#82ca9d",
        '&$checked': {
            color: "#82ca9d",
        },
    },
    checked: {},
})((props: CheckboxProps) => <Checkbox color="default" {...props} />);

const PurpleCheckbox = withStyles({
    root: {
        color: "#8884d8",
        '&$checked': {
            color: "#8884d8",
        },
    },
    checked: {},
})((props: CheckboxProps) => <Checkbox color="default" {...props} />);

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
        graphContainer: {
            display: 'flex',
            justifyContent: 'space-around',
            margin: '10px 10px',
        },
        avatarSize: {
            width: theme.spacing(15),
            height: theme.spacing(15),
        },
        graphTitleText: {
            textAlign: 'center',
            fontSize: '1.2em',
        },
    }),
);

const CodeAnalysis = () => {
    const classes = useStyles();
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const { projectId, startDateTime, endDateTime } =  router.query;

    const [projectName, setProjectName] = React.useState<String>();
    const [mergerRequestCount, setMergerRequestCount] = React.useState<number>();
    const [commitCount, setCommitCount] = React.useState<number>();
    const [mergeRequestScore, setMergeRequestScore] = React.useState<number>();
    const [commitScore, setCommitScore] = React.useState<number>();

    const [checkboxState, setCheckboxState] = React.useState({
        checkedCommitForGraphA: true,
        checkedMergeRequestForGraphA: true,
        checkedCommitForGraphB: true,
        checkedMergeRequestForGraphB: true,
    });

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProjectName(resp.data.name_with_namespace);
                }).catch(() => {
                    enqueueSnackbar('Failed to get project name.', {variant: 'error',});
            });
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_requests?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergerRequestCount(resp.data.length);
                }).catch(() => {
                    enqueueSnackbar('Failed to get merge request count.', {variant: 'error',});
            });
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/commits?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setCommitCount(resp.data.length);
                }).catch(() => {
                    enqueueSnackbar('Failed to get commits count.', {variant: 'error',});
            });
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/merge_requests/score?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequestScore(resp.data);
                }).catch(() => {
                    enqueueSnackbar('Failed to get merge request score.', {variant: 'error',});
            });
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/commits/score?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setCommitScore(resp.data);
                }).catch(() => {
                    enqueueSnackbar('Failed to get commits score.', {variant: 'error',});
            });
        }
    }, [projectId]);

    let graphA;
    let graphB;
    let graphATitle;
    let graphBTitle;

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCheckboxState({ ...checkboxState, [event.target.name]: event.target.checked });
    };

    if (checkboxState.checkedCommitForGraphA === true && checkboxState.checkedMergeRequestForGraphA === true) {
        graphATitle = "Daily Total Commits & Merge Requests Made By Everyone";
        graphA = <TotalCountGraph/>;
    } else if (checkboxState.checkedCommitForGraphA === true && checkboxState.checkedMergeRequestForGraphA === false) {
        graphATitle = "Daily Total Commits Made By Everyone";
        graphA = <CommitsCountGraph/>;
    } else if (checkboxState.checkedCommitForGraphA === false && checkboxState.checkedMergeRequestForGraphA === true) {
        graphATitle = "Daily Total Merge Requests Made By Everyone";
        graphA = <MergeRequestsCountGraph/>;
    } else if (checkboxState.checkedCommitForGraphA === false && checkboxState.checkedMergeRequestForGraphA === false){
        graphATitle = "Daily Total for Commits & Merge Requests Made By Everyone";
        graphA = <EmptyGraph/>;
    }

    if (checkboxState.checkedCommitForGraphB === true && checkboxState.checkedMergeRequestForGraphB === true) {
        graphBTitle = "Daily Total Score for Commits & Merge Requests Made By Everyone";
        graphB = <TotalScoreGraph/>;
    } else if (checkboxState.checkedCommitForGraphB === true && checkboxState.checkedMergeRequestForGraphB === false) {
        graphBTitle = "Daily Total Score for Commits Made By Everyone";
        graphB = <CommitsScoreGraph/>;
    } else if (checkboxState.checkedCommitForGraphB === false && checkboxState.checkedMergeRequestForGraphB === true) {
        graphBTitle = "Daily Total Score for Merge Requests Made By Everyone";
        graphB = <MergeRequestsScoreGraph/>;
    } else if (checkboxState.checkedCommitForGraphB === false && checkboxState.checkedMergeRequestForGraphB === false){
        graphBTitle = "Daily Total Score for Commits & Merge Requests Made By Everyone";
        graphB = <EmptyGraph/>;
    }

    return (
        <>
            <div className={classes.outerContainer}>
                <div className={classes.container1}>
                    <div className={classes.container2}>
                        <Avatar className={classes.avatarSize} variant='square'>R</Avatar>
                        <div className={classes.textContainer1}>
                            <h1 className={classes.repoNameText}>{projectName}</h1>
                            <p className={classes.smallTextColor}>- {commitCount} Commits - {mergerRequestCount} Merge Request -</p>
                        </div>
                    </div>
                    <div className={classes.textContainer2}>
                        <p className={classes.mrScoreText}>Merge Request Score: {mergeRequestScore}</p>
                        <p className={classes.commitScoreText}>Commit Score: {commitScore}</p>
                    </div>
                </div>
                <p className={classes.graphTitleText}>{graphATitle}</p>
                <div className={classes.graphContainer}>
                    {graphA}
                    <FormGroup>
                        <FormControlLabel
                            control={<GreenCheckbox checked={checkboxState.checkedCommitForGraphA} onChange={handleChange} name="checkedCommitForGraphA"/>}
                            label="Commits"
                        />
                        <FormControlLabel
                            control={<PurpleCheckbox checked={checkboxState.checkedMergeRequestForGraphA} onChange={handleChange} name="checkedMergeRequestForGraphA"/>}
                            label="Merge Requests"
                        />
                    </FormGroup>
                </div>
                <p className={classes.graphTitleText}>{graphBTitle}</p>
                <div className={classes.graphContainer}>
                    {graphB}
                    <FormGroup>
                        <FormControlLabel
                            control={<GreenCheckbox checked={checkboxState.checkedCommitForGraphB} onChange={handleChange} name="checkedCommitForGraphB"/>}
                            label="Commits"
                        />
                        <FormControlLabel
                            control={<PurpleCheckbox checked={checkboxState.checkedMergeRequestForGraphB} onChange={handleChange} name="checkedMergeRequestForGraphB"/>}
                            label="Merge Requests"
                        />
                    </FormGroup>
                </div>
            </div>
        </>
    );
};

export default CodeAnalysis;