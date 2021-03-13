import 'react-diff-view/style/index.css';
import {FileChange} from "../../interfaces/GitLabFileChange";
import {parseFileChangesForDiffViewer} from "./FileChangeParser";
import {ParsedFileChange} from "../../interfaces/ParsedFileChange";
// @ts-ignore (Doesn't have typescript types)
import {Decoration, Diff, Hunk} from 'react-diff-view';
import React, {useState} from "react";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";

type DiffViewerProps = { fileChanges: FileChange[] };
const DiffViewer = ({fileChanges}: DiffViewerProps) => {
    const [parsedFileChanges, setParsedFileChanges] = useState<ParsedFileChange[]>(parseFileChangesForDiffViewer(fileChanges));
    return (
        <>
            {parsedFileChanges.map((change) => <FileDiffView key={change.newPath + '-' + change.newRevision} change={change}/>)}
        </>
    )
}

type FileDiffViewProps = {change: ParsedFileChange}
const FileDiffView = ({change}: FileDiffViewProps) => {
    const styles = useStyles();

    return (
        <div className={styles.fileDiff}>
            <header className={styles.diffHeader}>{getChangeHeader(change)}</header>
            <Diff viewType="unified" diffType={change.type} hunks={change.hunks}>
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

const getChangeHeader = (parsedFileChange: ParsedFileChange): string => {
    if (parsedFileChange.oldPath === parsedFileChange.newPath) {
        return parsedFileChange.oldPath;
    } else {
        return `${parsedFileChange.oldPath} -> ${parsedFileChange.newPath}`
    }
}

export default DiffViewer;