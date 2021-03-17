import {FileChange} from "../../interfaces/GitLabFileChange";
import {parseFileChangesForDiffViewer} from "./FileChangeParser";
import React, {useState} from "react";
import {Box, Card, Divider, ListSubheader, Typography} from "@material-ui/core";
import LinkIcon from '@material-ui/icons/Link';
import List from "@material-ui/core/List";
import AppButton from "../app/AppButton";
import FileDiffView from "./FileDiffView";


type DiffViewerProps = { fileChanges: FileChange[], linkToFileChanges: string };
const DiffViewer = ({fileChanges, linkToFileChanges}: DiffViewerProps) => {
    const [isUnified, setUnified] = useState<boolean>(true);
    const parsedFileChanges = parseFileChangesForDiffViewer(fileChanges);
    return (
        <Card>
            <List
                component="nav"
                disablePadding
                subheader={
                    <ListSubheader style={{display: 'flex'}}>
                        <p>
                            Diff
                        </p>
                        <Box display="flex" width="100%" alignItems="center" justifyContent="flex-end">
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
                            >
                                <LinkIcon/>
                            </AppButton>
                        </Box>
                    </ListSubheader>
                }
            >
                <Divider/>
                <Box p={2} height="75vh" overflow="auto">
                    {
                        // Display text if no diff data present
                        parsedFileChanges.length == 0 &&
                        <Box width="100%" height="100%" display="flex" alignItems="center" justifyContent="center">
                            <Typography variant="h5">
                                Please select a merge request or commit to view diffs from.
                            </Typography>
                        </Box>
                    }
                    {
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

export default DiffViewer;