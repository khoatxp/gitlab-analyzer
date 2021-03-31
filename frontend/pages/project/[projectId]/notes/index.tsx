import {createStyles, makeStyles} from "@material-ui/core/styles";
import React, {useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import {
    Box,
    Card, Checkbox,
    Container,
    FormControl,
    FormControlLabel, FormGroup,
    Grid, Link,
    List,
    ListItem,
    ListItemText,
    ListSubheader,
    Radio,
    RadioGroup,
    Typography
} from "@material-ui/core";
import {Note} from "../../../../interfaces/GitLabNote";
import {useRouter} from "next/router";
import {AuthContext} from "../../../../components/AuthContext";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";
import formatDate from "../../../../utils/DateFormatter";
import {useSnackbar} from "notistack";

const useStyles = makeStyles(() =>
    createStyles({
        notesList: {
            overflow: 'auto',
            height: '80vh',
            width: '80vw',
        },
    }),
);

enum NoteType {
    MergeRequest,
    Issue
}

const NotesPage = () => {
    const {enqueueSnackbar} = useSnackbar();

    const [mergeRequestNotes, setMergeRequestNotes] = useState<Note[]>([]);
    const [issueNotes, setIssueNotes] = useState<Note[]>([]);

    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    const router = useRouter();
    const {projectId} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/${projectId}`;

    useEffect(() => {
        if (projectId) {
            axios
                .get(`${PROJECT_ID_URL}/merge_request_notes`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequestNotes(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get merge requests notes.', {variant: 'error',});
            });
            axios
                .get(`${PROJECT_ID_URL}/issue_notes`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setIssueNotes(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get issue notes.', {variant: 'error',});
            });
        }
    }, [projectId]);

    return (
        <AuthView>
            <MenuLayout tabSelected={2}>
                <Container maxWidth='xl'>
                    <Grid container spacing={2}>
                        <Card>
                            <NotesList
                                mergeRequestNotes={mergeRequestNotes}
                                issueNotes={issueNotes}
                            />
                        </Card>
                    </Grid>
                </Container>
            </MenuLayout>
        </AuthView>
    );
};

const RadioGroupSelectMergeRequestsOrIssues = ({value, handleChange}
                                                   : {
    value: NoteType,
    handleChange: React.Dispatch<React.ChangeEvent<HTMLInputElement>>,
}) => {

    return (
        <FormControl component="fieldset">
            <RadioGroup row
                        name="mergeRequestsOrIssues"
                        defaultValue={NoteType.MergeRequest}
                        value={value}
                        onChange={handleChange}>
                <FormControlLabel
                    value={NoteType.MergeRequest}
                    control={<Radio color="primary" size="small"/>}
                    label={`Merge request notes`}
                />
                <FormControlLabel
                    value={NoteType.Issue}
                    control={<Radio color="secondary" size="small"/>}
                    label={`Issue notes`}
                />
            </RadioGroup>
        </FormControl>
    );
};

const HideOwnCheckbox = ({hidden, handleChange}
                             : {
    hidden: boolean,
    handleChange: React.Dispatch<React.ChangeEvent<HTMLInputElement>>,
}) => {
    return (
        <FormControl component="fieldset">
            <FormGroup>
                <FormControlLabel
                    control={<Checkbox checked={hidden} onChange={handleChange} name="hide own" size="small"/>}
                    label='Hide "(Own)" notes'
                />
            </FormGroup>
        </FormControl>
    );
};

const NotesList = ({mergeRequestNotes, issueNotes}: {
    mergeRequestNotes: Note[],
    issueNotes: Note[],
}) => {

    const [noteType, setNoteType] = useState(NoteType.MergeRequest);
    const [hideOwnNotes, setHideOwnNotes] = useState(false);

    const handleChangeNoteType = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNoteType(Number((event.target as HTMLInputElement).value));
    };


    const classes = useStyles();
    return (
        <List subheader={<ListSubheader color={"primary"}>{
            <Box bgcolor="white">
                <RadioGroupSelectMergeRequestsOrIssues
                    value={noteType}
                    handleChange={handleChangeNoteType}
                />
                <HideOwnCheckbox hidden={hideOwnNotes} handleChange={() => setHideOwnNotes(!hideOwnNotes)}/>
            </Box>
        }</ListSubheader>}
              className={classes.notesList}
        >
            {(noteType === NoteType.MergeRequest ? mergeRequestNotes : issueNotes).map((note) => (
                hideOwnNotes && note.own ? <></> :
                    <ListItem key={note.id}>
                        <ListItemText
                            primary={
                                <>
                                    {`${note.author.name} `}
                                    <Typography
                                        component="span"
                                        variant="body2"
                                        color="textSecondary"
                                    >
                                        {`@${note.author.username}
                                         · ${formatDate(note.createdAt)}
                                         · ${getWordCount(note.body)} words · `
                                        }
                                    </Typography>
                                    <Link variant="body2"
                                          rel="noopener noreferrer"
                                          target="_blank"
                                          href={note.parentWebUrl}>{`#${note.parentIid}`}
                                    </Link>
                                    <Typography
                                        component="span"
                                        variant="body2"
                                        color="textSecondary"
                                    >
                                        {note.own ? " · (Own)" : " · (Other)"}
                                    </Typography>
                                </>}
                            secondary={
                                <>
                                    <Typography
                                        component="span"
                                        variant="body2"
                                        color="textPrimary"
                                        style={{wordWrap: 'break-word'}}
                                    >
                                        {note.body}
                                    </Typography>
                                </>}
                        />
                    </ListItem>
            ))}
        </List>
    );
};

const getWordCount = (text: string) => {
    return text.trim().split(/\s+/).length;
}

export default NotesPage;
