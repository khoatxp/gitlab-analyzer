import {ParsedFileChange} from "../../interfaces/ParsedFileChange";
// @ts-ignore (Doesn't have typescript types)
import {tokenize} from 'react-diff-view';
// @ts-ignore (Doesn't have typescript types)
import * as refractor from 'refractor';

export const Tokenize = (change: ParsedFileChange) => {
    if (!change.hunks) {
        return undefined;
    }

    const options = {
        refractor,
        highlight: true,
        language: getExtensionFromFilePath(change.newPath),
    };

    try {
        return tokenize(change.hunks, options);
    } catch (ex) {
        return undefined;
    }
}

const getExtensionFromFilePath = (path: string): string => {
    // Get everything after the last period
    return path.substr(path.lastIndexOf(".") + 1);
}