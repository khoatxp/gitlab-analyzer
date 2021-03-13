import 'react-diff-view/style/index.css';
import {FileChange} from "../../interfaces/GitLabFileChange";
import {ParsedFileChange, parseFileChangesForDiffViewer} from "./FileChangeParser";
// @ts-ignore (Doesn't have typescript types)
import {Decoration, Diff, Hunk} from 'react-diff-view';

type DiffViewerProps = { fileChanges: FileChange[] };
const DiffViewer = ({fileChanges}: DiffViewerProps) => {
    let parsedFileChanges: ParsedFileChange[] = parseFileChangesForDiffViewer(fileChanges);

    return (
        <>
            {parsedFileChanges.map((change) => (
                <div key={change.oldRevision + '-' + change.newRevision} className="file-diff">
                    <header className="diff-header">{getChangeHeader(change)}</header>
                    <Diff viewType="unified" diffType={change.type} hunks={change.hunks}>
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
            ))}
        </>
    )
}

const getChangeHeader = (parsedFileChange: ParsedFileChange): string => {
    if (parsedFileChange.oldPath === parsedFileChange.newPath) {
        return parsedFileChange.oldPath;
    } else {
        return `${parsedFileChange.oldPath} -> ${parsedFileChange.newPath}`
    }
}

export default DiffViewer;