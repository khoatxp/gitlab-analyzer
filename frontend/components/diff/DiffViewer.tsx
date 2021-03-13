// @ts-ignore (Doesn't have typescript types)
import {Decoration, Diff, Hunk, parseDiff} from 'react-diff-view';
import 'react-diff-view/style/index.css';
import {FileChange} from "../../interfaces/GitLabFileChange";
import assert from "assert";

type DiffViewerProps = {fileChanges: FileChange[]};
const DiffViewer = ({fileChanges}: DiffViewerProps) => {
    let allFilesToRender: any[] = [];
    for (const fileChange of fileChanges) {
        const fileToRender = parseFileChange(fileChange);
        allFilesToRender.push(fileToRender);
    }
    return allFilesToRender.map(renderFile);
};

const parseFileChange = (fileChange: FileChange) => {
    let diffText = fileChange.diff;
    diffText = addRequiredLinesForParsing(diffText, fileChange);
    const files = parseDiff(diffText);

    // Our file changes are only for one file so the array will be of length 1;
    files[0].oldPath = fileChange.old_path;
    files[0].newPath = fileChange.new_path;
    assert(files.length == 1);
    return files[0];
}

const addRequiredLinesForParsing = (diffText: string, fileChange: FileChange) => {
    return `--- ${fileChange.old_path}\n` +
        `+++ ${fileChange.new_path}\n` +
        diffText;
}

type renderFileProps = {
    gitlabFileChange: FileChange,
    oldPath: string,
    newPath: string,
    oldRevision: string,
    newRevision: string,
    type: string,
    hunks: any[]
}

const renderFile = ({oldPath, newPath, oldRevision, newRevision, type, hunks}: renderFileProps) => {
    console.log(oldPath);
    return (
        <div key={oldRevision + '-' + newRevision} className="file-diff">
        <header className="diff-header">{oldPath === newPath ? oldPath : `${oldPath} -> ${newPath}`}</header>
        <Diff viewType="unified" diffType={type} hunks={hunks}>
            {(hunks: any[]) =>
                hunks.map(hunk => [
                    <Decoration key={'deco-' + hunk.content}>
                        <div className="hunk-header">{hunk.content}</div>
                    </Decoration>,
                    <Hunk key={hunk.content} hunk={hunk}/>,
                ])
            }
        </Diff>
    </div>
    )
};

export default DiffViewer;