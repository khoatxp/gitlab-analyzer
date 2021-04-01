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


const index = () => {
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
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
        setItemBeingLoaded("Projects");
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/${serverId}/projects`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                // Couple of times I have seen this endpoint return a list of one item with all nulls. Have not been
                // able to reproduce consistently. Adding a condition check here to at least handle it on frontend
                if (resp.data.length === 1 && resp.data[0].id === null) {
                    setProjects([]);
                }
                else {
                    setProjects(resp.data);
                }
                setIsLoading(false);
            }).catch(() => {
            enqueueSnackbar('Failed to get server projects.', {variant: 'error',});
        });
    }

    const handleAnalyze = (projectIds: number[], startDateTime: Date, endDateTime: Date) => {
        // Display loading bar again
        setItemBeingLoaded("Analysis");
        setIsLoading(true);

        // Make callout and redirect after it is done. Note that the API call may take a while.
        const start = formatISO(startDateTime);
        const end = formatISO(endDateTime);
        const dateQuery = `startDateTime=${start}&endDateTime=${end}`;
        axios
            .post(`${process.env.NEXT_PUBLIC_API_URL}/${serverId}/projects/analytics?${dateQuery}`, projectIds, getAxiosAuthConfig())
            .then((res) => {
                let analyzedProjectIds = res.data;
                if (analyzedProjectIds.length > 1) {
                    // Multiple projects analyzed, go to analyses page
                    router.push(`/server/${serverId}/analyses`);
                } else {
                    // Single project analyzed, go to overview for the project
                    router.push(`/project/${analyzedProjectIds[0]}/0/overview?${dateQuery}`);
                }
            }).catch(() => {
            enqueueSnackbar('Failed to load analysis from server.', {variant: 'error',});
        });
    }

    return (
        <AuthView>
            <CardLayout backLink={`/server/${serverId}`} logoType="header">
                {isLoading && <LoadingBar itemBeingLoaded={itemBeingLoaded}/>}
                {!isLoading && <ProjectSelect projects={projects} onAnalyzeClick={handleAnalyze}/>}
            </CardLayout>
        </AuthView>
    );
};

export default index;
