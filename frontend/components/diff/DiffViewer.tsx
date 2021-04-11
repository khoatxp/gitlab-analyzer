import {FileChange} from "../../interfaces/GitLabFileChange";
import {parseFileChangesForDiffViewer} from "./FileChangeParser";
import React, {useState} from "react";
import {Box, Card, Divider, ListSubheader, Typography} from "@material-ui/core";
import LinkIcon from '@material-ui/icons/Link';
import List from "@material-ui/core/List";
import AppButton from "../app/AppButton";
import FileDiffView from "./FileDiffView";


type DiffViewerProps = {
    fileChanges: FileChange[],
    linkToFileChanges: string,
    isOrphanCommitsSelected: boolean,
    score: number,
};
const DiffViewer = ({fileChanges, linkToFileChanges, isOrphanCommitsSelected, score}: DiffViewerProps) => {
    const [isUnified, setUnified] = useState<boolean>(true);
    let infoText = getInfoText(fileChanges.length, isOrphanCommitsSelected);
    const parsedFileChanges = parseFileChangesForDiffViewer(fileChanges);
    return (
        <Card>
            <List
                component="nav"
                disablePadding
                subheader={
                    <ListSubheader style={{display: 'flex', justifyContent: "space-between"}}>
                        <p>
                            Diff(s)
                            {score !== 0 && <span> Score: <b>{score}</b></span> }
                        </p>
                        <Box display="flex" alignItems="center" justifyContent="flex-end">
                            <AppButton
                                disabled={fileChanges.length === 0}
                                color="primary"
                                size="small"
                                onClick={() => setUnified(!isUnified)}
                            >
                                {isUnified ? 'Split' : 'Unified'} View
                            </AppButton>
                            <AppButton
                                disabled={!linkToFileChanges}
                                color="primary"
                                size="small"
                                onClick={() => window.open(linkToFileChanges, '_blank')}
                                startIcon={<LinkIcon/>}
                            >
                                View in GitLab
                            </AppButton>
                        </Box>
                    </ListSubheader>
                }
            >
                <Divider/>
                <Box p={2} height="75vh" overflow="auto">
                    {
                        infoText != '' ?
                            <Box width="100%" height="100%" display="flex" alignItems="center" justifyContent="center">
                                <Typography variant="h5">
                                    {infoText}
                                </Typography>
                            </Box>
                            :
                            parsedFileChanges.map((change) => (
                                <FileDiffView
                                    unified={isUnified}
                                    key={change.newPath + '-' + change.newRevision}
                                    change={change}/>
                            ))
                    }
                </Box>
            </List>
        </Card>
    )
}

const getInfoText = (fileChangesLength: number, isOrphanCommitsSelected: boolean) => {
    let infoText = ''
    infoText = fileChangesLength == 0 ? 'Please select a merge request or commit to view diffs from.' : infoText;
    infoText = isOrphanCommitsSelected ? 'Please select an orphan commit from the commit list.' : infoText;
    return infoText
}
export default DiffViewer;