import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../../components/layout/CardLayout";
import {GitLabProject} from "../../../interfaces/GitLabProject";
import AuthView from "../../../components/AuthView";
import {AuthContext} from "../../../components/AuthContext";
import {useSnackbar} from 'notistack';
import ProjectSelect from "../../../components/ProjectSelect";
import LoadingBar from "../../../components/LoadingBar";
import {formatISO} from "date-fns";
// @ts-ignore
import SockJS from "sockjs-client";
// @ts-ignore
import Stomp from "stompjs";


const index = () => {
    const {user} = React.useContext(AuthContext);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [itemBeingLoaded, setItemBeingLoaded] = useState<string>('');
    const {serverId} = router.query;

    useEffect(() => {
        if(router.isReady) {
            loadProjects();
        }
    }, [serverId]);

    const loadProjects = () => {
        // TODO need to pass serverId into this call to get the correct gitlab url and access code from db
        // when that information is available in db
        setItemBeingLoaded("Projects");
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/${serverId}/projects`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setProjects(resp.data);
                setIsLoading(false);
            }).catch(() => {
            enqueueSnackbar('Failed to get server projects.', {variant: 'error',});
        });
    }

    const handleAnalyze = (projectIds: number[], startDateTime: Date, endDateTime: Date) => {
        // Make callout and redirect after it is done. Note that the API call may take a while.
        const start = formatISO(startDateTime);
        const end = formatISO(endDateTime);
        const dateQuery = `startDateTime=${start}&endDateTime=${end}`;

        axios
            .post(`${process.env.NEXT_PUBLIC_API_URL}/projects/analytics/generate_analysis_runs/?${dateQuery}`, projectIds, getAxiosAuthConfig())
            .then((res) => {
                axios
                    .post(`${process.env.NEXT_PUBLIC_API_URL}/projects/analytics/save_all`, res.data, getAxiosAuthConfig())
                    .catch(()=>{
                        enqueueSnackbar('Failed to load analysis from server.', {variant: 'error',});
                    })
                router.push(`/server/${serverId}/analyses`);
            }).catch(() => {
            enqueueSnackbar('Failed to generate projects runs.', {variant: 'error',});
        });
    }

    return (
        <AuthView>
            <CardLayout backLink={"/server"} logoType="header">
                {isLoading && <LoadingBar itemBeingLoaded={itemBeingLoaded}/>}
                {!isLoading && <ProjectSelect projects={projects} onAnalyzeClick={handleAnalyze}/>}
            </CardLayout>
        </AuthView>
    );
};

export default index;
