import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import React, {memo, useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import {
    Box,
    Card,
    Container,
    FormControl,
    FormControlLabel,
    Grid,
    List,
    ListItem,
    ListItemText,
    ListSubheader,
    Radio,
    RadioGroup,
    Typography
} from "@material-ui/core";
import {areEqual, FixedSizeList} from 'react-window';
import memoize from 'memoize-one';
import AutoSizer from 'react-virtualized-auto-sizer';
import {Note} from "../../../../interfaces/GitLabNote";
import {MergeRequest} from "../../../../interfaces/GitLabMergeRequest";
import {Issue} from "../../../../interfaces/GitLabIssue";
import {Task} from "../../../../interfaces/GitLabTask";
import {useRouter} from "next/router";
import {AuthContext} from "../../../../components/AuthContext";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";
import formatDate from "../../../../utils/DateFormatter";
import {useSnackbar} from "notistack";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        appBarSpacer: theme.mixins.toolbar,
        taskList: {
            overflow: 'auto',
            height: '75vh',
        },
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

    const [mergeRequestWordCounts, setMergeRequestWordCounts] = useState<number[]>([]);
    const [issueWordCounts, setIssueWordCounts] = useState<number[]>([]);

    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    const router = useRouter();
    const {projectId, startDateTime, endDateTime} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}`;

    const [selectedItem, setSelectedItem] = useState(0);

    const handleSelectNoteType = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNoteType(Number((event.target as HTMLInputElement).value));

    };

    useEffect(() => {
        handleSelectItem(0);
    }, [noteType]);

    const handleSelectItem = (
        index: number,
    ) => {
        setSelectedItem(index);
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
        handleSelectItem(0); // first merge request is selected on page load
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

    useEffect(() => {
        setMergeRequestWordCounts(
            mergeRequestNotes.map(
                (notes) =>
                    notes.reduce((totalWords, note) => (totalWords + getWordCount(note.body)), 0)
            )
        )
    }, [mergeRequestNotes]);

    useEffect(() => {
        setIssueWordCounts(
            issueNotes.map(
                (notes) =>
                    notes.reduce((totalWords, note) => (totalWords + getWordCount(note.body)), 0)
            )
        )
    }, [issueNotes]);

    const classes = useStyles();
    return (
        <AuthView>
            <MenuLayout tabSelected={2}>
                <Container maxWidth='xl'>
                    <Grid container spacing={2}>

                        <Grid item xs={12} md={4} lg={3}>
                            <Card>
                                <Box style={{display: 'flex', justifyContent: 'center'}}>
                                    <RadioGroupSelectMergeRequestsOrIssues
                                        value={noteType}
                                        handleChange={handleSelectNoteType}
                                        numMergeRequests={mergeRequests.length}
                                        numIssues={issues.length}
                                    />
                                </Box>
                                <Box className={classes.taskList}>
                                    <TaskList items={noteType === NoteType.MergeRequest ? mergeRequests : issues}
                                              selectedItem={selectedItem}
                                              handleSelectedItemChange={handleSelectItem}
                                              wordCounts={
                                                  noteType === NoteType.MergeRequest ?
                                                      mergeRequestWordCounts : issueWordCounts
                                              }
                                    />
                                </Box>
                            </Card>
                        </Grid>

                        <Grid item xs={12} md={8} lg={9}>
                            <Card>
                                <NotesList notes={
                                    noteType === NoteType.MergeRequest ?
                                        mergeRequestNotes[selectedItem] :
                                        issueNotes[selectedItem]
                                }
                                />
                            </Card>
                        </Grid>
                    </Grid>
                </Container>
            </MenuLayout>
        </AuthView>
    );
};

const RadioGroupSelectMergeRequestsOrIssues = ({value, handleChange, numMergeRequests, numIssues}
                                                   : {
    value: NoteType,
    handleChange: React.Dispatch<React.ChangeEvent<HTMLInputElement>>,
    numMergeRequests: number,
    numIssues: number,
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
                    label={`${numMergeRequests} Merge Requests`}
                />
                <FormControlLabel
                    value={NoteType.Issue}
                    control={<Radio color="secondary" size="small"/>}
                    label={`${numIssues} Issues`}
                />
            </RadioGroup>
        </FormControl>
    );
}

const Row = memo(({data, index, style}
                      : {
    data: { items: Task[], selectedItem: number, setSelectedItem: React.Dispatch<number>, wordCounts: number[] },
    index: number,
    style: React.CSSProperties
}) => {

    const {items, selectedItem, setSelectedItem, wordCounts} = data;
    const item = items[index];

    return (
        <ListItem
            button
            onClick={() => setSelectedItem(index)}
            selected={selectedItem === index}
            disableRipple
            style={style}
        >
            <ListItemText
                primary={
                    <>
                        {`${item.title} `}
                        <Typography
                            component="span"
                            variant="body2"
                            color="textSecondary"
                        >
                            {`· ${wordCounts[index] ?? "..."} words`}
                        </Typography>
                    </>}
                secondary={`#${item.iid} · opened ${formatDate(item.created_at)} by ${item.author.name}`}/>
        </ListItem>
    );
}, areEqual);

const createItemData = memoize((items: Task[],
                                selectedItem: number,
                                setSelectedItem: React.Dispatch<number>,
                                wordCounts: number[],
) => ({
    items,
    selectedItem,
    setSelectedItem,
    wordCounts,
}));

const TaskList = ({items, selectedItem, handleSelectedItemChange, wordCounts}
                      : {
                      items: Task[],
                      selectedItem: number,
                      handleSelectedItemChange: React.Dispatch<number>,
                      wordCounts: number[],
                  }
) => {

    const itemData = createItemData(items, selectedItem, handleSelectedItemChange, wordCounts);
    return (
        <AutoSizer>
            {({height, width}) => (
                <FixedSizeList
                    height={height}
                    itemCount={items.length}
                    itemData={itemData}
                    itemSize={120}
                    width={width}
                >
                    {Row}
                </FixedSizeList>
            )}
        </AutoSizer>
    );
}

const NotesList = ({notes}: { notes: Note[] }) => {
    const classes = useStyles();

    return (
        <List subheader={<ListSubheader disableSticky>{`${notes?.length ?? "..."} Notes`}</ListSubheader>}
              className={classes.notesList}
        >
            {notes?.map((note) => (
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
                                    {`@${note.author.username} · ${formatDate(note.created_at)} · ${getWordCount(note.body)} words`}
                                </Typography>
                            </>}
                        secondary={note.body}/>
                </ListItem>
            ))}
        </List>
    );
};

const getWordCount = (text: string) => {
    return text.trim().split(/\s+/).length;
}

export default NotesPage;
