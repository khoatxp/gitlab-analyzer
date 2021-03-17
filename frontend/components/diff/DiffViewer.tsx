import {FileChange} from "../../interfaces/GitLabFileChange";
import {parseFileChangesForDiffViewer} from "./FileChangeParser";
import {ParsedFileChange} from "../../interfaces/ParsedFileChange";
import React, {useMemo, useState} from "react";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import {Box, Card, Divider, ListSubheader, Typography} from "@material-ui/core";
import {Tokenize} from "./Tokenize";
import LinkIcon from '@material-ui/icons/Link';
// @ts-ignore (Doesn't have typescript types)
import {Decoration, Diff, Hunk} from 'react-diff-view';
import List from "@material-ui/core/List";
import AppButton from "../app/AppButton";

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

type FileDiffViewProps = { unified?: boolean, change: ParsedFileChange }
const FileDiffView = ({unified, change}: FileDiffViewProps) => {
    const styles = useStyles();
    const tokens = useMemo(() => Tokenize(change), [change]);   // Used for syntax highlighting

    return (
        <Card raised className={styles.fileDiff}>
            <Typography className={styles.diffHeader}>{getFileChangeHeader(change)}</Typography>
            <Diff viewType={ unified ? "unified" : 'split'} diffType={change.type} hunks={change.hunks} tokens={tokens}>
                {(hunks: any[]) =>
                    hunks.map(hunk => [
                        <Decoration key={'deco-' + hunk.content}>
                            <div className={styles.hunkHeader}>{hunk.content}</div>
                        </Decoration>,
                        <Hunk key={hunk.content} hunk={hunk}/>,
                    ])
                }
            </Diff>
        </Card>
    )
}

const getFileChangeHeader = (parsedFileChange: ParsedFileChange): string => {
    if (parsedFileChange.oldPath === parsedFileChange.newPath) {
        return parsedFileChange.oldPath;
    } else {
        return `${parsedFileChange.oldPath} -> ${parsedFileChange.newPath}`
    }
}

// Styling adapted from https://codesandbox.io/s/149oz0nw1q
const useStyles = makeStyles(() =>
    createStyles({
        fileDiff: {
            marginBottom: '1em',
            borderRadius: '10px',
            boxShadow: '20px',
        },
        diffHeader: {
            padding: '0.75em',
            borderBottom: '1px solid lightgray',
        },
        hunkHeader: {
            backgroundColor: '#f0f8ff',
            color: '#1b1f2380',
            padding: '5px 20px',
        },
    }),
);

export default DiffViewer;