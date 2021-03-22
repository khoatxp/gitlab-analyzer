import {Box, TextField} from "@material-ui/core";
import React, {useState} from "react";
import {GitLabProject} from "../interfaces/GitLabProject";
import Autocomplete from "@material-ui/lab/Autocomplete";
import AppDateTimePicker from "./app/AppDateTimePicker";
import AppButton from "./app/AppButton";
import ScoreProfileSelect from "./ScoreProfileSelect";
import ScoreProfile from "../interfaces/ScoreProfile";

type ProjectSelectProps = {
    projects: GitLabProject[]
    onAnalyzeClick: (projectIds: number[], startDateTime: Date, endDateTime: Date) => void
}

const ProjectSelect = ({projects, onAnalyzeClick}: ProjectSelectProps) => {
    const now = new Date();
    const [selectedProjectId, setSelectedProjectId] = useState<number>(0);
    const [startDateTime, setStartDateTime] = useState<Date>(new Date(now.getFullYear(), now.getMonth() - 1, now.getDate()));
    const [endDateTime, setEndDateTime] = useState<Date>(now);
    const [scoreProfile, setScoreProfile] = useState<ScoreProfile | undefined>();
    const [scoreProfileId, setScoreProfileId] = useState<number | null>();

    const onProjectSelect = (_event: any, value: GitLabProject) => {
        setSelectedProjectId(value ? value.id: 0); // Value will be null when the clear button is pressed. Ensure we have a number
    }

    const onProfileSelect = (event: any, profile: ScoreProfile) => {
        setScoreProfile(profile);
        setScoreProfileId(profile ? profile.id: 0);
    }

    return (
        <>
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
                marginTop="10px"
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
                    onStartDateTimeChange={dateTime => setStartDateTime(dateTime)}
                    onEndDateTimeChange={dateTime => setEndDateTime(dateTime)}
                    startDateTime={startDateTime}
                    endDateTime={endDateTime}
                />

                <ScoreProfileSelect
                    scoreProfile={scoreProfile}
                    onScoreProfileSelect={onProfileSelect}
                 />

            </Box>
            <Box
                alignSelf="center"
                marginTop="10px"
            >
                <AppButton
                    color="primary"
                    disabled={selectedProjectId === 0}
                    onClick={() => onAnalyzeClick([selectedProjectId], startDateTime, endDateTime)}
                >
                    Analyze
                </AppButton>
            </Box>
        </>
    )
};

export default ProjectSelect;