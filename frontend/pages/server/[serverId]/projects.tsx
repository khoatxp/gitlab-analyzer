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
import ScoreProfileSelector from "../../../components/ScoreProfileSelector";
import ScoreProfile from "../../../interfaces/ScoreProfile";

const index = () => {
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const now = new Date();
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [selectedProjectId, setSelectedProjectId] = useState<number>(0);
    const [startDateTime, setStartDateTime] = useState<Date>(new Date(now.getFullYear(), now.getMonth() - 1, now.getDate()));
    const [endDateTime, setEndDateTime] = useState<Date>(now);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [profile, setProfile] = useState<ScoreProfile>();
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

    let loadingBar = null;
    if (isLoading) {
        loadingBar = <LoadingBar/>;
    }

    const onProjectSelect = (_event: any, value: GitLabProject) => {
        setSelectedProjectId(value.id);
    }

    const onStartDateTimeSelect = (start: Date) => {
        setStartDateTime(start);
    }

    const onEndDateTimeSelect = (end: Date) => {
        setEndDateTime(end);
    }

    const onProfileSelect = (x: ScoreProfile) => {
        setProfile(x);
    }

    const onAnalyzeClicked = () => {
        const start = formatISO(startDateTime);
        const end = formatISO(endDateTime);
        router.push(`/project/${selectedProjectId}/code?startDateTime=${start}&endDateTime=${end}`);
    }

    return (
        <AuthView>
            <CardLayout>
                {loadingBar}
                {!isLoading && <>
                    <Autocomplete
                        id="project-select"
                        onChange={onProjectSelect}
                        options={projects}
                        getOptionLabel={(proj) => proj.name_with_namespace}
                        renderInput={(params) => <TextField {...params} label="Search Projects" variant="outlined"/>}
                    />

                    <Box
                        marginLeft="5px"
                        marginRight="5px"
                        marginTop="3px"
                        color="white"
                        boxShadow={0}
                        width="56vw"
                        minWidth="260px"
                        display="flex"
                        flexDirection="row"
                        justifyContent="row"
                        alignItems="center"
                    >
                        <AppDateTimePicker
                            onStartDateTimeChange={onStartDateTimeSelect}
                            onEndDateTimeChange={onEndDateTimeSelect}
                            startDateTime={startDateTime}
                            endDateTime={endDateTime}
                        />


                        <ScoreProfileSelector
                            profile={profile}
                            setProfile={onProfileSelect}
                        />


                    </Box>
                    <Box
                        alignSelf="center"
                        marginTop="10px">
                        <AppButton color="primary" disabled={selectedProjectId === 0}
                                   onClick={onAnalyzeClicked}>Analyze</AppButton>
                    </Box>
                </>}
            </CardLayout>
        </AuthView>
    );
};

const LoadingBar = () => {
    return <div>
        <Typography variant={"body1"}>
            Loading projects...
        </Typography>
        <LinearProgress/>
    </div>;
}

export default index;
