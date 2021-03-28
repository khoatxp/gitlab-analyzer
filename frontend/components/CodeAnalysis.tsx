import React, {useEffect} from "react";
import axios, {AxiosResponse} from "axios";
import {useRouter} from "next/router";
import {useSnackbar} from "notistack";
import {AuthContext} from "./AuthContext";
import CountGraph from "../components/graphs/CountGraph";
import ScoreGraph from "../components/graphs/ScoreGraph";
import AnalysisSummary, {ProjectSummary} from "./AnalysisSummary";
import {GitLabProject} from "../interfaces/GitLabProject";
import {ScoreDigest} from "../interfaces/ScoreDigest";

const CodeAnalysis = () => {
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    // TODO startDateTime, endDateTime could be possibly undefined (missing from url) or an array(multiple)
    // Need to handle these cases. This might be a non issue or handled differently with analysis run
    const {projectId, gitManagementUserId, startDateTime, endDateTime} = router.query;

    const [project, setProject] = React.useState<GitLabProject>();
    const [mergeRequestCount, setMergeRequestCount] = React.useState<number>(0);
    const [commitCount, setCommitCount] = React.useState<number>(0);
    const [mergeRequestScore, setMergeRequestScore] = React.useState<number>(0);
    const [sharedMergeRequestScore, setSharedMergeRequestScore] = React.useState<number>(0);
    const [commitScore, setCommitScore] = React.useState<number>(0);
    const [scoreDigest, setScoreDigest] = React.useState<ScoreDigest[]>([]);

    const getDateTime = (queryDatetime: string | string[] | undefined) => {
        if (Array.isArray(queryDatetime)) {
            return queryDatetime[0];
        }
        return queryDatetime;

    };

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

            // TODO Pass correct Score Profile Id
            let scoreProfileId = 0;
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/score_digest/${scoreProfileId}?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setScoreDigest(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get score digest.', {variant: 'error',});
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
            <CountGraph data={scoreDigest}
                        startDateTime={getDateTime(startDateTime)}
                        endDateTime={getDateTime(endDateTime)}/>
            <ScoreGraph data={scoreDigest}
                        startDateTime={getDateTime(startDateTime)}
                        endDateTime={getDateTime(endDateTime)}/>
        </>
    );
};

export default CodeAnalysis;