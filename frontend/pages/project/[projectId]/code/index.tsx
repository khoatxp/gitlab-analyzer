import React, {useEffect} from "react";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";
import {AuthContext} from "../../../../components/AuthContext";
import axios, {AxiosResponse, AxiosError} from "axios";
import {useRouter} from "next/router";
// @ts-ignore (No types available)
import {MergeRequest} from "../../../../interfaces/GitLabMergeRequest";
import {useSnackbar} from "notistack";
import DiffViewer from "../../../../components/diff/DiffViewer";

const index = () => {
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [mergeRequests, setMergeRequests] = React.useState<MergeRequest[]>([]);
    const [diffText, setDiffText] = React.useState<String>('');
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
        // Must have merge requests
        if (mergeRequests.length == 0) { return; }
        console.log("GETTING DIFF", mergeRequests[0].iid);
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${mergeRequests[0].iid}/diff`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setDiffText(resp.data[1].diff);
                // console.log(resp.data[1].diff);
            }).catch(() => {
                enqueueSnackbar("Failed to load data", {variant: 'error'});
            });
    }, [mergeRequests]);

    return (
        <AuthView>
            <MenuLayout tabSelected={1}>
                {diffText && <DiffViewer diffText={diffText}/>}
            </MenuLayout>
        </AuthView>
    );
};

export default index;
