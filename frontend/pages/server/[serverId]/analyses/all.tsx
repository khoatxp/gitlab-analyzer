import AuthView from "../../../../components/AuthView";
import CardLayout from "../../../../components/layout/CardLayout";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {Avatar, Box, Paper, Typography} from "@material-ui/core";
import axios, {AxiosResponse} from "axios";
import {useSnackbar} from "notistack";
import {AuthContext} from "../../../../components/AuthContext";
import AppButton from "../../../../components/app/AppButton";
import formatDate from "../../../../utils/DateFormatter";
import AnalysisRunStatusIndicator from "../../../../components/AnalysisRunStatusIndicator";
import {AnalysisRun, AnalysisRunStatus} from "../../../../interfaces/AnalysisRun";

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
            .get(`${process.env.NEXT_PUBLIC_API_URL}/analysis_run/${serverId}/all`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                console.log(resp.data);
                setAnalysisRuns(resp.data)
            }).catch(() => {
            enqueueSnackbar('Failed to get runs.', {variant: 'error',});
        }).finally(() => {
            setIsLoading(false);
        });
    }

    console.log(analysisRuns);
    return (
        <AuthView>
            <CardLayout backLink={`/server/${serverId}`} logoType="header" size="lg">
                Test
            </CardLayout>
        </AuthView>
    )
}

export default index;