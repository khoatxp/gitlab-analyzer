import {Box, LinearProgress, TextField, Typography} from "@material-ui/core";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../../components/layout/CardLayout";
import AppDateTimePicker from "../../../components/app/AppDateTimePicker";
import AppButton from "../../../components/app/AppButton";
import {GitLabProject} from "../../../interfaces/GitLabProject";
import Autocomplete from "@material-ui/lab/Autocomplete";
import AuthView from "../../../components/AuthView";
import {AuthContext} from "../../../components/AuthContext";
import {useSnackbar} from 'notistack';
import {formatISO} from "date-fns";
import ProjectSelect from "../../../components/ProjectSelect";
import LoadingBar from "../../../components/LoadingBar";

const index = () => {
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [itemBeingLoaded, setItemBeingLoaded] = useState<string>('');
    const {serverId} = router.query;

    useEffect(() => {
        if (router.isReady) {
            // TODO need to pass serverId into this call to get the correct gitlab url and access code from db
            // when that information is available in db
            setItemBeingLoaded("Projects");
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProjects(resp.data);
                    setIsLoading(false);
                }).catch(() => {
                enqueueSnackbar('Failed to get server projects.', {variant: 'error',});
            });
        }
    }, [serverId]);

    const handleAnalyze = (projectId: number, startDateTime: Date, endDateTime: Date) => {
        setItemBeingLoaded("Analysis");
        setIsLoading(true);
        // const start = formatISO(startDateTime);
        // const end = formatISO(endDateTime);
        // router.push(`/project/${projectId}/code?startDateTime=${start}&endDateTime=${end}`);
    }

    return (
        <AuthView>
            <CardLayout>
                {isLoading && <LoadingBar itemBeingLoaded={itemBeingLoaded}/>}
                {!isLoading && <ProjectSelect projects={projects} onAnalyzeClick={handleAnalyze}/>}
            </CardLayout>
        </AuthView>
    );
};
export default index;
