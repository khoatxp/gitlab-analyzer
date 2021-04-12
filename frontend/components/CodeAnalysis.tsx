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
import LoadingView from "./LoadingView";
import formatDate from "../utils/DateFormatter"

const CodeAnalysis = () => {
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    // TODO startDateTime, endDateTime could be possibly undefined (missing from url) or an array(multiple)
    // Need to handle these cases. This might be a non issue or handled differently with analysis run
    const {projectId, gitManagementUserId, startDateTime, endDateTime, scoreProfileId} = router.query;

    const [project, setProject] = React.useState<GitLabProject>();
    const [mergeRequestCount, setMergeRequestCount] = React.useState<number>(0);
    const [commitCount, setCommitCount] = React.useState<number>(0);
    const [mergeRequestScore, setMergeRequestScore] = React.useState<number>(0);
    const [sharedMergeRequestScore, setSharedMergeRequestScore] = React.useState<number>(0);
    const [commitScore, setCommitScore] = React.useState<number>(0);
    const [scoreDigest, setScoreDigest] = React.useState<ScoreDigest[]>([]);
    const [memberUserName, setMemberUserName] = React.useState<string>("Loading...");
    const [isRetrieving, setIsRetrieving] = React.useState<boolean>(true);

    const getParameter = (queryParameter: string | string[] | undefined) => {
        if (Array.isArray(queryParameter)) {
            return queryParameter[0];
        }
        return queryParameter;

    };
    const getMemberUsername =() =>{
        const selected = document.getElementsByClassName('selectedMemberButton');
        const memberUsername = selected[0]?.textContent;
        return memberUsername != null ? memberUsername : "";
    }

    useEffect(() => {
        if (router.isReady) {

            setIsRetrieving(true);
            const dateQuery = `startDateTime=${startDateTime}&endDateTime=${endDateTime}`;

            const requestProject = axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}`, getAxiosAuthConfig());
            const requestMergeRequestCount = axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/merge_request/user/${gitManagementUserId}/count?${dateQuery}`, getAxiosAuthConfig());
            const requestCommitCount = axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/commits/user/${gitManagementUserId}/count?${dateQuery}`, getAxiosAuthConfig());
            const requestMergeRequestScore = axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/merge_request/user/${gitManagementUserId}/diff/score/${scoreProfileId}?${dateQuery}`, getAxiosAuthConfig());
            const requestCommitScore = axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/commit/user/${gitManagementUserId}/diff/score/${scoreProfileId}?${dateQuery}`, getAxiosAuthConfig());
            const requestScoreDigest = axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/score_digest/user/${gitManagementUserId}/${scoreProfileId}?${dateQuery}`, getAxiosAuthConfig())

            axios
                .all([requestProject, requestMergeRequestCount, requestCommitCount, requestMergeRequestScore, requestCommitScore, requestScoreDigest])
                .then(axios.spread((...resp) => {
                setProject(resp[0].data);
                setMergeRequestCount(resp[1].data);
                setCommitCount(resp[2].data);
                setMergeRequestScore(resp[3].data?.mergeScore);
                setSharedMergeRequestScore(resp[3].data?.sharedMergeScore);
                setCommitScore(resp[4].data);
                setScoreDigest(resp[5].data);
                setMemberUserName(getMemberUsername);
                setIsRetrieving(false);
            })).catch(() => {
                enqueueSnackbar('Failed to get project data.', {variant: 'error',});
                setIsRetrieving(false);
            })

        }
    }, [projectId, gitManagementUserId]);

    let projectSummary: ProjectSummary = {
        project: project,
        commitCount: commitCount,
        mergeRequestCount: mergeRequestCount,
        commitScore: commitScore,
        mergeRequestScore: mergeRequestScore,
        sharedMergeRequestScore: sharedMergeRequestScore,
        gitManagementUserId: getParameter(gitManagementUserId),
        memberUserName: memberUserName,
        dateRange: `${formatDate(getParameter(startDateTime) ?? "")} - ${formatDate(getParameter(endDateTime) ?? "")}`
    }

    return (
        <>
            <LoadingView isRetrieving={isRetrieving}/>
            <AnalysisSummary projectSummary={projectSummary}/>
            <CountGraph data={scoreDigest}
                        startDateTime={getParameter(startDateTime)}
                        endDateTime={getParameter(endDateTime)}
                        memberUserName={memberUserName}/>
            <ScoreGraph data={scoreDigest}
                        startDateTime={getParameter(startDateTime)}
                        endDateTime={getParameter(endDateTime)}
                        memberUserName={memberUserName}/>
        </>
    );
};

export default CodeAnalysis;