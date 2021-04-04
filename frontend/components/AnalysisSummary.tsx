import React from "react";
import Avatar from '@material-ui/core/Avatar';
import {GitLabProject} from "../interfaces/GitLabProject";
import {Box, Chip, IconButton, Typography} from "@material-ui/core";
import {makeStyles} from '@material-ui/core/styles';
import StarIcon from '@material-ui/icons/Star';
import RestaurantIcon from '@material-ui/icons/Restaurant';
import FileCopyIcon from '@material-ui/icons/FileCopy';
import {useSnackbar} from "notistack";
import getMember from "../utils/memberName";

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
    sharedMergeRequestScore: number;
    gitManagementUserId: string | undefined,
}

const AnalysisSummary = ({projectSummary}: { projectSummary: ProjectSummary }) => {
    const {project, commitCount, mergeRequestCount, commitScore, mergeRequestScore, sharedMergeRequestScore, gitManagementUserId} = projectSummary;
    const {enqueueSnackbar} = useSnackbar();
    const styles = useStyles();

    const handleCopyScore = async () => {
        try {
            let scoreText = `Merge Request Count: ${mergeRequestCount}\n`;
            scoreText += `Merge Request Score: ${mergeRequestScore} + ${sharedMergeRequestScore}\n`;
            scoreText += `Commit Count: ${commitCount}\n`;
            scoreText += `Commit Score: ${commitScore}`;
            await navigator.clipboard.writeText(scoreText);
            enqueueSnackbar("Score summary copied to clipboard", {variant: "success"})
        } catch {
            enqueueSnackbar("Failed to copy score summary to clipboard", {variant: "error"})
        }
    }
    const displayMerge = () => {

    }


    return <Box display="flex" alignItems="center" padding={4}>
        <Avatar className={styles.avatar} variant='rounded' src={project?.avatar_url ?? ''}>
            <Typography variant="h3">
                {getAvatarText(project?.avatar_url ?? '', project?.name ?? '')}
            </Typography>
        </Avatar>

        <Box ml={3} flexGrow={1}>
            <Typography variant="h3">{project?.name_with_namespace ?? "Loading..."}</Typography>
            <Typography variant="h5">{getMember(`${gitManagementUserId}`) ?? "Loading..."}</Typography>
            <Typography variant="subtitle2">
                {commitCount} Commit(s) -{' '}
                {mergeRequestCount} Merge Request(s)
            </Typography>
            <Box pt={1}>
                <Chip style={{marginRight: "5px"}} color="primary" size="small" icon={<StarIcon/>}
                      label={`Stars: ${project?.star_count ?? 0}`}/>
                <Chip color="primary" size="small" icon={<RestaurantIcon/>}
                      label={`Forks: ${project?.forks_count ?? 0}`}/>
            </Box>
        </Box>

        <Box display="flex" alignItems="center">
            <Box>
                <Typography variant="h6"><b>Merge Request Score:</b></Typography>
                <Typography >{gitManagementUserId != '0' && gitManagementUserId != undefined? `Own: ${mergeRequestScore}` : ""}</Typography>
                <Typography >{gitManagementUserId != '0' && gitManagementUserId != undefined? `Shared: ${sharedMergeRequestScore}` : ""}</Typography>
                <Typography >Total: {sharedMergeRequestScore + mergeRequestScore}</Typography>
                <Typography variant="h6"><b>Commit Score:</b> {commitScore}</Typography>
            </Box>
            <Box ml={2}>
                <IconButton color="primary" size="medium" onClick={handleCopyScore} disabled={!project}>
                    <FileCopyIcon/>
                </IconButton>
            </Box>
        </Box>
    </Box>;
};

const getAvatarText = (avatarUrl: string, name: string) => {
    // Provide the first letter of the name like GitLab if no avatarUrl is present
    if (!avatarUrl) {
        // Use '...' if no name is present
        return name ? name[0].toUpperCase() : '...';
    } else {
        return avatarUrl
    }
}

export default AnalysisSummary;