import {createStyles, makeStyles} from "@material-ui/core/styles";
import {ParsedFileChange} from "../../interfaces/ParsedFileChange";
import React, {useMemo, useState} from "react";
import {Tokenize} from "./Tokenize";
import {Card, IconButton, Typography} from "@material-ui/core";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowRightIcon from "@material-ui/icons/ArrowRight";
// @ts-ignore (Doesn't have typescript types)
import {Decoration, Diff, Hunk} from 'react-diff-view';

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

type FileDiffViewProps = { unified: boolean, change: ParsedFileChange }
const FileDiffView = ({unified, change}: FileDiffViewProps) => {
    const styles = useStyles();
    const [hidden, setHidden] = useState<boolean>(false);
    const tokens = useMemo(() => Tokenize(change), [change]);   // Used for syntax highlighting

    return (
        <Card raised className={styles.fileDiff}>
            <Typography className={styles.diffHeader}>
                <IconButton size="small" onClick={() => setHidden(!hidden)}>
                    { hidden ? <ArrowDropDownIcon/> : <ArrowRightIcon/> }
                </IconButton>
                {getFileChangeHeader(change)}
            </Typography>

            {
                !hidden &&
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
            }
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

export default FileDiffView;