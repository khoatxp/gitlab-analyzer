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
    const {projectId, gitManagementUserId, startDateTime, endDateTime} = router.query;

    const [project, setProject] = React.useState<GitLabProject>();
    const [mergeRequestCount, setMergeRequestCount] = React.useState<number>(0);
    const [commitCount, setCommitCount] = React.useState<number>(0);
    const [mergeRequestScore, setMergeRequestScore] = React.useState<number>(0);
    const [sharedMergeRequestScore, setSharedMergeRequestScore] = React.useState<number>(0);
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
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/merge_request/user/${gitManagementUserId}?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequestCount(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get merge request count.', {variant: 'error',});
            });
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/commits/user/${gitManagementUserId}?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setCommitCount(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get commits count.', {variant: 'error',});
            });
            //TODO update 0 in endpoint to profile id
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/merge_request/user/${gitManagementUserId}/diff/score/0?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequestScore(resp.data?.mergeScore);
                    setSharedMergeRequestScore(resp.data?.sharedMergeScore);
                }).catch(() => {enqueueSnackbar('Failed to get merge request score.', {variant: 'error',});
            });
            //TODO update 0 in endpoint to profile id
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/commit/user/${gitManagementUserId}/diff/score/0?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setCommitScore(resp.data);
                }).catch(() => {enqueueSnackbar('Failed to get commits score.', {variant: 'error',});
            });
        }
    }, [projectId, gitManagementUserId]);

    let projectSummary: ProjectSummary = {
        project: project,
        commitCount: commitCount,
        mergeRequestCount: mergeRequestCount,
        commitScore: commitScore,
        mergeRequestScore: mergeRequestScore,
        sharedMergeRequestScore: sharedMergeRequestScore,
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