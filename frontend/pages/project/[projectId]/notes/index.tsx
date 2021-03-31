import {createStyles, makeStyles} from "@material-ui/core/styles";
import React, {useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import {
    Card,
    Container,
    FormControl,
    FormControlLabel,
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

    const [noteType, setNoteType] = useState(NoteType.MergeRequest);

    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    const router = useRouter();
    const {projectId} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/${projectId}`;

    const handleSelectNoteType = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNoteType(Number((event.target as HTMLInputElement).value));

    };

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
                                notes={noteType === NoteType.MergeRequest ?
                                    mergeRequestNotes : issueNotes
                                }
                                noteType={noteType}
                                handleChangeNoteType={handleSelectNoteType}
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


const NotesList = ({notes, noteType, handleChangeNoteType}: {
    notes: Note[],
    noteType: NoteType,
    handleChangeNoteType: React.Dispatch<React.ChangeEvent<HTMLInputElement>>,
}) => {

    const classes = useStyles();
    return (
        <List subheader={<ListSubheader color={"primary"}>{
            <RadioGroupSelectMergeRequestsOrIssues
                value={noteType}
                handleChange={handleChangeNoteType}
            />
        }</ListSubheader>}
              className={classes.notesList}
        >
            {notes.map((note) => (
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
                                      href={note.parentWebUrl}>{`#${note.parentIid}`}</Link>
                            </>}
                        secondary={
                            <>
                                <Typography
                                    component="span"
                                    variant="body2"
                                    color="textPrimary"
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
