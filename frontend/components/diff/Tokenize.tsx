// @ts-ignore (Doesn't have typescript types)
import {tokenize} from 'react-diff-view';
// @ts-ignore (Doesn't have typescript types)
import refractor from 'refractor';

// Based on a tokenization with options example taken from library creator
// https://codesandbox.io/s/react-diff-view-mark-edits-demo-forked-yc5rc?file=/src/tokenize.js:1648-1978
export default (hunks: any, fileExtension: string) => {
    if (!hunks) {
        return undefined;
    }

    const options = {
        highlight: true,
        refractor: refractor,
        language: fileExtension,
    };

    try {
        return tokenize(hunks, options);
    } catch (ex) {
        return undefined;
    }
};