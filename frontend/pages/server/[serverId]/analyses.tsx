import AuthView from "../../../components/AuthView";
import CardLayout from "../../../components/layout/CardLayout";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {Avatar, Box, Paper, Typography} from "@material-ui/core";
import axios, {AxiosResponse} from "axios";
import {useSnackbar} from "notistack";
import {AuthContext} from "../../../components/AuthContext";
import AppButton from "../../../components/app/AppButton";
import formatDate from "../../../utils/DateFormatter";
import AnalysisRunStatus from "../../../components/AnalysisRunStatus";

const index = () => {
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const {serverId} = router.query;
    const [analyses, setAnalyses] = useState([]);

    useEffect(() => {
        if (!router.isReady) {
            return;
        }

        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/analysis_run/${serverId}`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setAnalyses(resp.data)
            }).catch(() => {
            enqueueSnackbar('Failed to get runs.', {variant: 'error',});
        });
    }, [serverId]);

    return (
        <AuthView>
            <CardLayout backLink={`/server/${serverId}`} logoType="header" size="lg">
                <Box maxHeight="50vh" overflow="auto">
                {
                    analyses.map((analysis: any) =>
                        <Paper elevation={4} style={{margin: "1em"}} key={analysis.id}>
                            <Box display="flex" alignItems="center" padding={2.5}>
                                <Avatar variant='rounded' style={{width: '4em', height: '4em'}}>
                                    <Typography variant="h3">
                                        {analysis.projectNameWithNamespace[0].toUpperCase()}
                                    </Typography>
                                </Avatar>

                                <Box ml={3} flexGrow={1}>
                                    <Typography variant="h4">{analysis.projectNameWithNamespace}</Typography>
                                    <Typography variant="subtitle2">
                                        <b>From:</b> {formatDate(analysis.startDateTime)} - {formatDate(analysis.endDateTime)}
                                    </Typography>
                                    <Typography variant="subtitle2">
                                        <b>Created:</b> {formatDate(analysis.createdDateTime)}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center">
                                    <AnalysisRunStatus status={'Complete'}/>
                                    <AppButton
                                        color="primary"
                                        onClick={() => router.push(`/project/${analysis.projectId}/overview?startDateTime=${analysis.startDateTime}&endDateTime=${analysis.endDateTime}`)}
                                    >
                                        View
                                    </AppButton>
                                </Box>
                            </Box>
                        </Paper>
                    )
                }
                </Box>
            </CardLayout>
        </AuthView>
    )
}

export default index;