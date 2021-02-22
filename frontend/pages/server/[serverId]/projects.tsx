import {Box, TextField, Typography, LinearProgress} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import React, {useState, useEffect} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../components/CardLayout";
import AppDateTimePicker from "../../components/AppDateTimePicker";
import AppButton from "../../components/AppButton";
import {GitLabProject} from "../../interfaces/GitLabProject";
import CardLayout from "../../../components/CardLayout";
import AppDateTimePicker from "../../../components/AppDateTimePicker";
import {GitLabProject} from "../../../interfaces/GitLabProject";
import Autocomplete from "@material-ui/lab/Autocomplete";

const useStyles = makeStyles((theme) => ({
    AnalyzeButtonMargin: {
        margin: theme.spacing(1),
    },
}));

const LoadingBar = () => {
    return <div>
        <Typography variant={"body1"}>
            Loading projects...
        </Typography>
        <LinearProgress/>
    </div>;
}


const index = () => {
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const router = useRouter();
    const {serverId} = router.query;

    useEffect(() => {
        if (router.isReady) {
            // TODO need to pass serverId into this call to get the correct gitlab url and access code from db
            // when that information is available in db
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects`)
                .then((resp: AxiosResponse) => {
                    setProjects(resp.data);
                    setIsLoading(false);
                });
        }
    }, [serverId]);

    let loadingBar = null;
    if (isLoading) {
        loadingBar = <LoadingBar/>;
    }

    return (
        <CardLayout>
            {loadingBar}
            {!isLoading && <>
                <Autocomplete
                    id="project-select"
                    options={projects}
                    getOptionLabel={(proj) => proj.name_with_namespace}
                    renderInput={(params) => <TextField {...params} label="Search Projects" variant="outlined"/>}
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

    );
}
;
export default index;
