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
import AppProgressBar from "../../../components/app/AppProgressBar";
import {formatISO} from "date-fns";
// @ts-ignore
import SockJS from "sockjs-client";
// @ts-ignore
import Stomp from "stompjs";
import AnimatedProgressText from "../../../components/AnimatedProgressText";
import Box from "@material-ui/core/Box";

const index = () => {
    const {user} = React.useContext(AuthContext);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const [progress, setProgress] = React.useState<number>(0);
    const [progressMessage, setProgressMessage] = React.useState<string>("Waiting for update from server");
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isAnalyzing, setIsAnalyzing] = useState<boolean>(false);
    const [itemBeingLoaded, setItemBeingLoaded] = useState<string>('');
    const {serverId} = router.query;

    useEffect(() => {
        if(router.isReady) {
            if(user){
                axios
                    .get(`${process.env.NEXT_PUBLIC_API_URL}/projects/analytics/progress/${user.id}`,getAxiosAuthConfig())
                    .then((res: AxiosResponse) => {
                        if(res.data){
                            setIsAnalyzing(true);
                            setProgress(res.data.progress);
                            setProgressMessage(res.data.message);
                        }else{
                            loadProjects();
                        }
                    }).catch((err) => {
                    enqueueSnackbar(`Failed to get projects analysis progress: ${err.message}`, {variant: 'error',});
                });

                const socket = new SockJS(`${process.env.NEXT_PUBLIC_BACKEND_URL}/websocket`);
                const stompClient = Stomp.over(socket);

                //turn off debugging logs on browser console
                stompClient.debug = () => {}

                stompClient.connect(getAxiosAuthConfig(), () => {
                    if(user){
                        stompClient.subscribe(`/topic/progress/${user.id}`, function (message:any) {
                            setIsAnalyzing(true);

                            const body = JSON.parse(message.body);
                            setProgress(body.progress);
                            setProgressMessage(body.message);

                            if(body.projectId){
                                const dateQuery = `startDateTime=${body.startDateTime}&endDateTime=${body.endDateTime}`;
                                router.push(`/project/${body.projectId}/overview?${dateQuery}`);
                            }
                        });
                    }
                });
            }
        }
    }, [serverId, user]);

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
        setIsAnalyzing(true);

        // Make callout and redirect after it is done. Note that the API call may take a while.
        const start = formatISO(startDateTime);
        const end = formatISO(endDateTime);
        const dateQuery = `startDateTime=${start}&endDateTime=${end}`;

        axios
            .post(`${process.env.NEXT_PUBLIC_API_URL}/projects/analytics?${dateQuery}`, projectIds, getAxiosAuthConfig())
            .then((res) => {
            }).catch((err) => {
            enqueueSnackbar('Failed to load analysis from server', {variant: 'error',});
        });
    }

    return (
        <AuthView>
            <CardLayout backLink={"/server"} logoType="header">
                {!isAnalyzing && isLoading && <LoadingBar itemBeingLoaded={itemBeingLoaded}/>}
                {!isAnalyzing && !isLoading && <ProjectSelect projects={projects} onAnalyzeClick={handleAnalyze}/>}
                {
                    isAnalyzing &&
                        <>
                            <AppProgressBar variant="determinate" value={progress}/>
                            <Box margin="8px">
                                <AnimatedProgressText progress={progress}> {progressMessage} </AnimatedProgressText>
                            </Box>
                        </>
                }
            </CardLayout>
        </AuthView>
    );
};

export default index;
