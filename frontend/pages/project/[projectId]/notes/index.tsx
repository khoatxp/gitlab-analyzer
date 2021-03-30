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
import {MergeRequest} from "../../../../interfaces/GitLabMergeRequest";
import {Issue} from "../../../../interfaces/GitLabIssue";
import {useRouter} from "next/router";
import {AuthContext} from "../../../../components/AuthContext";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";
import formatDate from "../../../../utils/DateFormatter";
import {useSnackbar} from "notistack";
import {Task} from "../../../../interfaces/GitLabTask";

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

    const [mergeRequests, setMergeRequests] = useState<MergeRequest[]>([]);
    const [issues, setIssues] = useState<Issue[]>([]);
    const [mergeRequestNotes, setMergeRequestNotes] = useState<Note[][]>([]);
    const [issueNotes, setIssueNotes] = useState<Note[][]>([]);

    const [noteType, setNoteType] = useState(NoteType.MergeRequest);

    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    const router = useRouter();
    const {projectId, startDateTime, endDateTime} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}`;

    const handleSelectNoteType = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNoteType(Number((event.target as HTMLInputElement).value));

    };

    useEffect(() => {
        if (projectId) {
            const dateQuery = `?startDateTime=${startDateTime}&endDateTime=${endDateTime}`;
            axios
                .get(`${PROJECT_ID_URL}/merge_requests${dateQuery}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequests(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get merge requests data.', {variant: 'error',});
            });
            axios
                .get(`${PROJECT_ID_URL}/issues${dateQuery}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setIssues(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to get issues data.', {variant: 'error',});
            });
        }
    }, [projectId]);

    useEffect(() => {
        getAllMergeRequestNotes();
    }, [mergeRequests]);

    useEffect(() => {
        getAllIssueNotes();
    }, [issues]);

    const getAllMergeRequestNotes = () => {
        axios.all(mergeRequests.map(
            (mergeRequest) => (
                axios
                    .get(`${PROJECT_ID_URL}/merge_requests/${mergeRequest.iid}/notes`, getAxiosAuthConfig()))
            )
        ).then((responses) => {
            setMergeRequestNotes(responses.map((resp) => (resp.data)));
        }).catch(() => {
            enqueueSnackbar('Failed to get merge request notes.', {variant: 'error',});
        });
    };

    const getAllIssueNotes = () => {
        axios.all(issues.map((issue) => (
            axios
                .get(`${PROJECT_ID_URL}/issues/${issue.iid}/notes`, getAxiosAuthConfig())))
        ).then((responses) => {
            setIssueNotes(responses.map((resp) => (resp.data)));
        }).catch(() => {
            enqueueSnackbar('Failed to get issue notes.', {variant: 'error',});
        });
    };

    return (
        <AuthView>
            <MenuLayout tabSelected={2}>
                <Container maxWidth='xl'>
                    <Grid container spacing={2}>
                        <Card>
                            <NotesList
                                noteArrays={noteType === NoteType.MergeRequest ?
                                    mergeRequestNotes : issueNotes
                                }
                                noteParents={
                                    noteType === NoteType.MergeRequest ?
                                        mergeRequests : issues
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


const NotesList = ({noteArrays, noteParents, noteType, handleChangeNoteType}: {
    noteArrays: Note[][],
    noteParents: Task[],
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
            {noteArrays?.map((noteArray, i) => (
                noteArray.map((note) => (
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
                                         · ${formatDate(note.created_at)}
                                         · ${getWordCount(note.body)} words · `
                                        }
                                    </Typography>
                                    <Link variant="body2"
                                          rel="noopener noreferrer"
                                          target="_blank"
                                          href={noteParents[i].web_url}>{`#${noteParents[i].iid}`}</Link>
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
                ))))}
        </List>
    );
};

const getWordCount = (text: string) => {
    return text.trim().split(/\s+/).length;
}

export default NotesPage;
