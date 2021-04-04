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
import AnalysisProgressModal from "../../../components/AnalysisProgressModal";
import {AnalysisRun} from "../../../interfaces/AnalysisRun";


const index = () => {
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [generatedAnalysisRun, setGeneratedAnalysisRun] = useState<AnalysisRun | null>(null);
    const [open, setOpen] = useState(false);
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
        setItemBeingLoaded("");
        setIsLoading(true);

        const start = formatISO(startDateTime);
        const end = formatISO(endDateTime);
        const dateQuery = `startDateTime=${start}&endDateTime=${end}`;

        axios
            .post(`${process.env.NEXT_PUBLIC_API_URL}/${serverId}/projects/analytics/generate_analysis_runs/?${dateQuery}`, projectIds, getAxiosAuthConfig())
            .then((res) => {
                axios
                    .post(`${process.env.NEXT_PUBLIC_API_URL}/${serverId}/projects/analytics/save_all`, res.data, getAxiosAuthConfig())
                    .catch(()=>{
                        enqueueSnackbar('Failed to load analysis from server.', {variant: 'error'});
                    })
                if(res.data.length > 1){
                    router.push(`/server/${serverId}/analyses`);
                } else if (res.data.length === 1){
                    setGeneratedAnalysisRun(res.data[0]);
                    setOpen(true);
                    setIsLoading(false);
                }
                else{
                    setIsLoading(false);
                    enqueueSnackbar(`Failed to analyze ${projectIds.length > 0 ? 'projects' : 'project'}`, {variant: 'error',});
                }
            }).catch(() => {
                setIsLoading(false);
                enqueueSnackbar('Failed to generate projects runs.', {variant: 'error',});
        });
    }

    const handleClose = () => {
        setOpen(false);
    };

    const handleError = () => {
        setOpen(false);
        const projectNameWithNamespace = generatedAnalysisRun?.projectNameWithNamespace;
        enqueueSnackbar(`Failed to analyze project ${projectNameWithNamespace}`,{variant: 'error'})
    };

    const redirectToOverviewPage = () => {
        const projectNameWithNamespace = generatedAnalysisRun?.projectNameWithNamespace;
        const projectId = generatedAnalysisRun?.projectId;
        const start = generatedAnalysisRun?.startDateTime;
        const end = generatedAnalysisRun?.endDateTime;
        const dateQuery = `startDateTime=${start}&endDateTime=${end}`;

        //Sometimes it is delayed for a few seconds until the page is redirected so we inform the user
        setOpen(false);
        setIsLoading(true);
        setItemBeingLoaded(`Overview page for ${projectNameWithNamespace}`);

        router.push(`/project/${projectId}/0/overview?${dateQuery}`);
    };

    return (
        <AuthView>
            {open && <AnalysisProgressModal open={open} handleClose={handleClose} handleWhenProgressIsDone={redirectToOverviewPage} handleError={handleError} analysisRun={generatedAnalysisRun}/>}
            <CardLayout backLink={`/server/${serverId}`} logoType="header">
                {isLoading && <LoadingBar itemBeingLoaded={itemBeingLoaded}/>}
                {!isLoading && <ProjectSelect projects={projects} onAnalyzeClick={handleAnalyze}/>}
            </CardLayout>
        </AuthView>
    );
};

export default index;
