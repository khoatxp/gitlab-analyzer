import React from "react";
import Avatar from '@material-ui/core/Avatar';
import {GitLabProject} from "../interfaces/GitLabProject";
import {Box, Chip, IconButton, Typography} from "@material-ui/core";
import {makeStyles} from '@material-ui/core/styles';
import StarIcon from '@material-ui/icons/Star';
import RestaurantIcon from '@material-ui/icons/Restaurant';
import FileCopyIcon from '@material-ui/icons/FileCopy';

const useStyles = makeStyles(theme => ({
    avatar: {
        width: theme.spacing(15),
        height: theme.spacing(15),
    },
}));

export interface ProjectSummary {
    project: GitLabProject | undefined,
    commitCount: number;
    mergeRequestCount: number;
    commitScore: number;
    mergeRequestScore: number;
}

const AnalysisSummary = ({projectSummary}: { projectSummary: ProjectSummary }) => {
    const styles = useStyles();


    if (!projectSummary.project) {
        return <></>
    }
    return (
        <Box display="flex" alignItems="center" padding={4}>
            <Avatar className={styles.avatar} variant='rounded' src={projectSummary.project.avatar_url}>
                <Typography variant="h3">
                    {getAvatarText(projectSummary.project.avatar_url, projectSummary.project.name)}
                </Typography>
            </Avatar>

            <Box ml={3} flexGrow={1}>
                <Typography variant="h3">{projectSummary.project.name_with_namespace}</Typography>
                <Typography variant="subtitle2">
                    {projectSummary.commitCount} Commit(s) -{' '}
                    {projectSummary.mergeRequestCount} Merge Request(s)
                </Typography>
                <Box pt={1}>
                    <Chip style={{marginRight: "5px"}} color="primary" size="small" icon={<StarIcon/>} label={`Stars: ${projectSummary.project.star_count}`}/>
                    <Chip color="primary" size="small" icon={<RestaurantIcon/>} label={`Forks: ${projectSummary.project.forks_count}`}/>
                </Box>
            </Box>

            <Box display="flex" alignItems="center">
                <Box>
                    <Typography variant="h6"><b>Merge Request Score:</b> {projectSummary.mergeRequestScore}</Typography>
                    <Typography variant="h6"><b>Commit Score:</b> {projectSummary.commitScore}</Typography>
                </Box>
                <Box ml={2}>
                    <IconButton color="primary" size="medium">
                        <FileCopyIcon />
                    </IconButton>
                </Box>
            </Box>
        </Box>
    );
};

const getAvatarText = (avatarUrl: string, name: string) => {
    // Provide the first letter of the name like GitLab if no avatarUrl is present
    if (avatarUrl == null) {
        return name[0].toUpperCase();
    } else {
        return ''
    }
}

export default AnalysisSummary;