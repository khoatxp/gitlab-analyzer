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

const index = () => {
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [mergeRequests, setMergeRequests] = React.useState<MergeRequest[]>([]);
    const [selectedMergeRequest, setSelectedMergeRequest] = React.useState<MergeRequest | null>(null);
    const [fileChanges, setFileChanges] = React.useState<FileChange[]>([]);
    const { projectId, startDateTime, endDateTime } =  router.query;

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

    useEffect(() => {
        // Ensure merge requests exist
        if (selectedMergeRequest === null) { return; }

        console.log("GETTING NEW ONES")
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${selectedMergeRequest.iid}/diff`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setFileChanges(resp.data);
            }).catch(() => {
                enqueueSnackbar("Failed to load data", {variant: 'error'});
            });
    }, [selectedMergeRequest]);

    const handleSelectMergeRequest = (mergeRequest: MergeRequest) => {
        console.log("HANDLING SELECTION")
        setSelectedMergeRequest(mergeRequest);
    }

    return (
        <AuthView>
            <MenuLayout tabSelected={1}>
                <MergeRequestList
                    mergeRequests={mergeRequests}
                    handleSelectMergeRequest={handleSelectMergeRequest}
                />
                {fileChanges.length > 0 && <DiffViewer fileChanges={fileChanges}/>}
            </MenuLayout>
        </AuthView>
    );
};

export default index;
