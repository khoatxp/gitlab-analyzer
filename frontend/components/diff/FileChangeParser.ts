// @ts-ignore (Doesn't have typescript types)
import { parseDiff } from 'react-diff-view';
import { FileChange } from "../../interfaces/GitLabFileChange";
import assert from "assert";
import {ParsedFileChange} from "../../interfaces/ParsedFileChange";

export const parseFileChangesForDiffViewer = (fileChanges: FileChange[]): ParsedFileChange[] => {
    // Function to help parse our GitLab file changes to a format react-diff-view expects
    let allFilesToRender: any[] = [];
    for (const fileChange of fileChanges) {
        const fileToRender = parseFileChange(fileChange);
        allFilesToRender.push(fileToRender);
    }
    return allFilesToRender;
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
    // Cannot have extra lines if file change is empty
    if (diffText.length == 0) { return diffText; }

    // The library expects these lines to begin the diff but GitLab does not include them
    return `--- ${fileChange.old_path}\n` +
        `+++ ${fileChange.new_path}\n` +
        diffText;
}
