import {Box, LinearProgress, TextField, Typography} from "@material-ui/core";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../components/CardLayout";
import AppDateTimePicker from "../../components/AppDateTimePicker";
import AppButton from "../../components/AppButton";
import {GitLabProject} from "../../interfaces/GitLabProject";
import Autocomplete from "@material-ui/lab/Autocomplete";
import AuthView from "../../components/AuthView";
import {AuthContext} from "../../components/AuthContext";
import {useSnackbar} from 'notistack';

const index = () => {
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const router = useRouter();
    const {serverId} = router.query;

    useEffect(() => {
        if (router.isReady) {
            // TODO need to pass serverId into this call to get the correct gitlab url and access code from db
            // when that information is available in db
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

    return (
        <AuthView>
            <CardLayout>
                {isLoading ? <LoadingBar/> :
                    <>
                        <Autocomplete
                            id="project-select"
                            options={projects}
                            getOptionLabel={(proj) => proj.name_with_namespace}
                            renderInput={(params) => <TextField {...params} label="Search Projects"
                                                                variant="outlined"/>}
                        />

                        <Box
                            marginLeft="5px"
                            marginRight="5px"
                            marginTop="10px"
                            color="white"
                            boxShadow={0}
                            width="56vw"
                            minWidth="260px"
                            display="flex"
                            flexDirection="column"
                            justifyContent="column"
                            alignItems="column"
                        >
                            <AppDateTimePicker/>
                        </Box>
                        <Box
                            alignSelf="center"
                            marginTop="10px">
                            <AppButton color="primary">Analyze</AppButton>
                        </Box>
                    </>}
            </CardLayout>
        </AuthView>

    );
}

const LoadingBar = () => {
    return <div>
        <Typography variant={"body1"}>
            Loading projects...
        </Typography>
        <LinearProgress/>
    </div>;
}

export default index;
