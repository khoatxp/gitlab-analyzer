import React, {useEffect} from "react";
import axios, {AxiosResponse} from "axios";
import {useRouter} from "next/router";
import {useSnackbar} from "notistack";
import {AuthContext} from "./AuthContext";
import CountGraph from "../components/graphs/CountGraph";
import ScoreGraph from "../components/graphs/ScoreGraph";
import AnalysisSummary, {ProjectSummary} from "./AnalysisSummary";
import {GitLabProject} from "../interfaces/GitLabProject";

const CodeAnalysis = () => {
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const {projectId, startDateTime, endDateTime} = router.query;

    const [project, setProject] = React.useState<GitLabProject>();
    const [mergerRequestCount, setMergerRequestCount] = React.useState<number>(0);
    const [commitCount, setCommitCount] = React.useState<number>(0);
    const [mergeRequestScore, setMergeRequestScore] = React.useState<number>(0);
    const [commitScore, setCommitScore] = React.useState<number>(0);

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProject(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get project data.', {variant: 'error',});
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

    let projectSummary: ProjectSummary = {
        project: project,
        commitCount: commitCount,
        mergeRequestCount: mergerRequestCount,
        commitScore: commitScore,
        mergeRequestScore: mergeRequestScore,
    }

    return (
        <>
            <AnalysisSummary projectSummary={projectSummary}/>
            <CountGraph/>
            <ScoreGraph/>
        </>
    );
};

export default CodeAnalysis;