import {Box} from "@material-ui/core";
// @ts-ignore (Doesn't have typescript types)
import {parseDiff, Diff, Hunk, Decoration} from 'react-diff-view';
import 'react-diff-view/style/index.css';

type DiffViewerProps = {diffText: String};
const DiffViewer = ({diffText}: DiffViewerProps) => {
    diffText = '--- a/bug_buddy/commands.py\n' +
        '+++ a/bug_buddy/commands.py\n' +
        diffText
    console.log("NEW\n\n", diffText);
    const files = parseDiff(diffText);
    console.log("FILES", files);
    return files.map(renderFile);
};

type renderFileInputParameters = {
    oldPath: string,
    newPath: string,
    oldRevision: string,
    newRevision: string,
    type: string,
    hunks: any[]
}

const renderFile = ({oldPath, newPath, oldRevision, newRevision, type, hunks}: renderFileInputParameters) => {

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