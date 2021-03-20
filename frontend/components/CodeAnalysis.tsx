import React, {useEffect} from "react";
import { makeStyles, Theme, createStyles } from '@material-ui/core/styles';
import Avatar from '@material-ui/core/Avatar';
import axios, {AxiosResponse} from "axios";
import {useRouter} from "next/router";
import {useSnackbar} from "notistack";
import {AuthContext} from "./AuthContext";
import CountGraph from "../components/graphs/CountGraph";
import ScoreGraph from "../components/graphs/ScoreGraph";
import {ScoreDigest} from "../interfaces/ScoreDigest";

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
        avatarSize: {
            width: theme.spacing(15),
            height: theme.spacing(15),
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
    const [scoreDigest, setScoreDigest] = React.useState<ScoreDigest[]>();

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

            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/score_digest/?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setScoreDigest(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get score digest.', {variant: 'error',});
            });
        }
    }, [projectId]);

    return (
        <>
            <div className={classes.outerContainer}>
                <div className={classes.container1}>
                    <div className={classes.container2}>
                        <Avatar className={classes.avatarSize} variant='rounded'>R</Avatar>
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
                <CountGraph/>
                <ScoreGraph/>
            </div>
        </>
    );
};

export default CodeAnalysis;