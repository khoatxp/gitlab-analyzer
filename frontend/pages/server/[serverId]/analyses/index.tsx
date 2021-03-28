import AuthView from "../../../../components/AuthView";
import CardLayout from "../../../../components/layout/CardLayout";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosError, AxiosResponse} from "axios";
import {useSnackbar} from "notistack";
import {AuthContext} from "../../../../components/AuthContext";
import {AnalysisRun} from "../../../../interfaces/AnalysisRun";
import AnalysisRunList from "../../../../components/analysis_run/AnalysisRunList";

const index = () => {
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const {serverId} = router.query;
    const [analysisRuns, setAnalysisRuns] = useState<AnalysisRun[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    useEffect(() => {
        loadAnalysisRuns();
    }, [serverId]);

    const loadAnalysisRuns = () => {
        if (!router.isReady || isLoading) {
            return;
        }
        setIsLoading(true);
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/analysis_run/${serverId}`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setAnalysisRuns(resp.data)
            }).catch((err: AxiosError) => {
            enqueueSnackbar(`Failed to get runs: ${err}`, {variant: 'error',});
        }).finally(() => {
            setIsLoading(false);
        });
    }

    console.log(analysisRuns);
    return (
        <AuthView>
            <CardLayout backLink={`/server/${serverId}`} logoType="header" size="lg">
                <AnalysisRunList isLoading={isLoading} analysisRuns={analysisRuns} loadAnalysisRuns={loadAnalysisRuns}/>
            </CardLayout>
        </AuthView>
    )
}

export default index;
