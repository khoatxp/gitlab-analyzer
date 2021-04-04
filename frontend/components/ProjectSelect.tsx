import {Box, Chip, TextField} from "@material-ui/core";
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
    const [selectedProjects, setSelectedProjects] = useState<GitLabProject[]>([]);
    const [startDateTime, setStartDateTime] = useState<Date>(new Date(now.getFullYear(), now.getMonth() - 1, now.getDate()));
    const [endDateTime, setEndDateTime] = useState<Date>(now);
    const [scoreProfileId, setScoreProfileId] = useState<number>(0);

    const onProjectSelect = (_event: any, value: GitLabProject) => {
        if (!value) { return; } // Value will be null when the clear button is pressed. Ensure we have a number

        // Check if we are already set to analyze the selected project
        if (selectedProjects.some(proj => proj.id == value.id)) {
            return;
        }

        const projectIds = [...selectedProjects, value]
        setSelectedProjects(projectIds)
    }

    const handleDeleteChip = (index: number) => {
        let newProjects = [...selectedProjects];
        newProjects.splice(index, 1);
        setSelectedProjects(newProjects);
    }

    const onScoreProfileSelect = (id: number) => {
        setScoreProfileId(id);
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
            <Box m={1.5} display="flex" flexWrap="wrap">
                {selectedProjects.map((project, index) =>
                    <Box m={0.5} key={project.id}>
                        <Chip
                            label={project.name_with_namespace}
                            onDelete={() => handleDeleteChip(index)}
                            color="primary"
                        />
                    </Box>
                )}
            </Box>
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
                    onScoreProfileSelect={onScoreProfileSelect}
                 />

            </Box>
            <Box
                alignSelf="center"
                marginTop="10px"
            >
                <AppButton
                    color="primary"
                    disabled={selectedProjects.length === 0}
                    onClick={() => onAnalyzeClick(selectedProjects.map(project => project.id), startDateTime, endDateTime)}
                >
                    Analyze
                </AppButton>
            </Box>
        </>
    )
};

export default ProjectSelect;