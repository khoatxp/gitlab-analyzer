import {FileChange} from "../../interfaces/GitLabFileChange";
import {parseFileChangesForDiffViewer} from "./FileChangeParser";
import {ParsedFileChange} from "../../interfaces/ParsedFileChange";
import React, {useMemo} from "react";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import {Box} from "@material-ui/core";
import {Tokenize} from "./Tokenize";
// @ts-ignore (Doesn't have typescript types)
import {Decoration, Diff, Hunk} from 'react-diff-view';

type DiffViewerProps = { fileChanges: FileChange[] };
const DiffViewer = ({fileChanges}: DiffViewerProps) => {
    const parsedFileChanges = parseFileChangesForDiffViewer(fileChanges);
    return (
        <Box
            border={1}
            borderColor={'lightGray'}
            maxHeight="50vh"
            overflow="auto"
            p={2}
        >
            {parsedFileChanges.map((change) => <FileDiffView key={change.newPath + '-' + change.newRevision}
                                                             change={change}/>)}
        </Box>
    )
}

type FileDiffViewProps = { change: ParsedFileChange }
const FileDiffView = ({change}: FileDiffViewProps) => {
    const styles = useStyles();
    const tokens = useMemo(() => Tokenize(change), [change]);   // Used for syntax highlighting

    return (
        <div className={styles.fileDiff}>
            <header className={styles.diffHeader}>{getFileChangeHeader(change)}</header>
            <Diff viewType="unified" diffType={change.type} hunks={change.hunks} tokens={tokens}>
                {(hunks: any[]) =>
                    hunks.map(hunk => [
                        <Decoration key={'deco-' + hunk.content}>
                            <div className={styles.hunkHeader}>{hunk.content}</div>
                        </Decoration>,
                        <Hunk key={hunk.content} hunk={hunk}/>,
                    ])
                }
            </Diff>
        </div>
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
const borderRadius = '10px';
const useStyles = makeStyles(() =>
    createStyles({
        fileDiff: {
            border: '1px solid lightgray',
            marginBottom: '1em',
            borderRadius: borderRadius,
        },
        diffHeader: {
            backgroundColor: 'white',
            padding: '1em',
            borderBottom: '1px solid lightgray',
            borderRadius: `${borderRadius} ${borderRadius} 0 0 `,
        },
        hunkHeader: {
            backgroundColor: '#f0f8ff',
            color: '#1b1f2380',
            padding: '5px 20px',
        },
    }),
);

export default DiffViewer;