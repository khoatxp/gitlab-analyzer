import React, {useEffect} from "react";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";
import {AuthContext} from "../../../../components/AuthContext";
import axios, {AxiosResponse} from "axios";
import {useRouter} from "next/router";
// @ts-ignore (No types available)
import {MergeRequest} from "../../../../interfaces/GitLabMergeRequest";
import {useSnackbar} from "notistack";
import DiffViewer from "../../../../components/diff/DiffViewer";
import {FileChange} from "../../../../interfaces/GitLabFileChange";
import MergeRequestList from "../../../../components/diff/MergeRequestList";
import CommitList from "../../../../components/diff/CommitList";
import {Commit} from "../../../../interfaces/GitLabCommit";

const index = () => {
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [mergeRequests, setMergeRequests] = React.useState<MergeRequest[]>([]);
    const [commits, setCommits] = React.useState<Commit[]>([]);
    const [fileChanges, setFileChanges] = React.useState<FileChange[]>([]);
    const {projectId, startDateTime, endDateTime} = router.query;

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_requests?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequests(resp.data);
                }).catch(() => {
                enqueueSnackbar("Failed to load data", {variant: 'error'});
            });
        }
    }, [projectId]);

    const fetchDiffDataFromUrl = (url: string) => {
        axios
            .get(url, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setFileChanges(resp.data);
            }).catch(() => {
            enqueueSnackbar("Failed to load data", {variant: 'error'});
        });
    };

    const fetchCommitData = (mergeRequest: MergeRequest) => {
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${mergeRequest.iid}/commits`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                console.log(resp.data);
                setCommits(resp.data);
            }).catch(() => {
            enqueueSnackbar("Failed to load data", {variant: 'error'});
        });
    }

    const handleSelectMergeRequest = (mergeRequest: MergeRequest) => {
        fetchCommitData(mergeRequest);
        // fetchDiffDataFromUrl(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${mergeRequest.iid}/diff`);
    };

    const handleSelectCommit = (commit: Commit) => {
        fetchDiffDataFromUrl(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${commit.id}/diff`);
    };

    return (
        <AuthView>
            <MenuLayout tabSelected={1}>
                <MergeRequestList
                    mergeRequests={mergeRequests}
                    handleSelectMergeRequest={handleSelectMergeRequest}
                />

                <CommitList commits={commits} handleSelectCommit={handleSelectCommit}/>
                {fileChanges.length > 0 && <DiffViewer fileChanges={fileChanges}/>}
            </MenuLayout>
        </AuthView>
    );
};

export default index;
