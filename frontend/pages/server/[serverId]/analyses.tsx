import AuthView from "../../../components/AuthView";
import CardLayout from "../../../components/layout/CardLayout";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {Avatar, Box, Paper, Typography} from "@material-ui/core";
import axios, {AxiosResponse} from "axios";
import {useSnackbar} from "notistack";
import {AuthContext} from "../../../components/AuthContext";
import AppButton from "../../../components/app/AppButton";

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
                console.log(resp.data)
                setAnalyses(resp.data)
            }).catch(() => {
            enqueueSnackbar('Failed to get runs.', {variant: 'error',});
        });
    }, [serverId]);

    return (
        <AuthView>
            <CardLayout backLink={`/server/${serverId}`} logoType="header">
                <Box style={{maxHeight: "300px", overflow: "auto"}}>
                {
                    analyses.map((analysis: any) =>
                        <Paper elevation={5} style={{margin: "0.75em"}}>
                            <Box display="flex" alignItems="center" padding={3}>
                                <Avatar variant='rounded' style={{width: '4em', height: '4em'}}>
                                    <Typography variant="h3">
                                        {analysis.projectNameWithNamespace[0].toUpperCase()}
                                    </Typography>
                                </Avatar>

                                <Box ml={3} flexGrow={1}>
                                    <Typography variant="h4">{analysis.projectNameWithNamespace}</Typography>
                                    <Typography variant="subtitle2">
                                        {analysis.startDateTime}
                                    </Typography>
                                    <Typography variant="subtitle2">
                                        {analysis.endDateTime}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center">
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