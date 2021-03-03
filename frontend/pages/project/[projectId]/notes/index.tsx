import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import React, {memo, useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";

import NavBar from "../../../../components/NavBar";
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
import NavTabs from "../../../../components/NavTabs";
import {AuthContext} from "../../../../components/AuthContext";
import AuthView from "../../../../components/AuthView";

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
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const {projectId, startDateTime, endDateTime} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}`;

    const [mergeRequests, setMergeRequests] = useState<MergeRequest[]>([]);
    const [issues, setIssues] = useState<Issue[]>([]);
    const [notes, setNotes] = useState<Note[]>([]);

    const [noteType, setNoteType] = React.useState(NoteType.MergeRequest);

    const handleSelectNoteType = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNoteType(Number((event.target as HTMLInputElement).value));
    };

    useEffect(() => {
        handleSelectItem(0);
    }, [noteType]);

    const [selectedItem, setSelectedItem] = useState(0);

    const getMergeRequestNotes = (mergeRequestIid: number) => {
        axios
            .get(`${PROJECT_ID_URL}/merge_requests/${mergeRequestIid}/notes`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setNotes(resp.data);
            })
    };

    const getIssueNotes = (issueIid: number) => {
        axios
            .get(`${PROJECT_ID_URL}/issues/${issueIid}/notes`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setNotes(resp.data);
            });
    };

    const handleSelectItem = (
        index: number,
    ) => {
        setSelectedItem(index);
        if (noteType === NoteType.MergeRequest) {
            if (mergeRequests?.[index]?.iid) {
                getMergeRequestNotes(mergeRequests[index].iid);
            }
        } else {
            if (issues?.[index]?.iid) {
                getIssueNotes(issues[index].iid);
            }
        }
    };

    useEffect(() => {
        if (projectId) {
            const dateQuery = `?startDateTime=${startDateTime ? startDateTime : "2021-01-01T00:00:00-08:00"}&endDateTime=${endDateTime ? endDateTime : "2021-03-21T00:00:00-08:00"}`;
            axios
                .get(`${PROJECT_ID_URL}/merge_requests${dateQuery}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequests(resp.data);
                });
            axios
                .get(`${PROJECT_ID_URL}/issues${dateQuery}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setIssues(resp.data);
                });
        }

    }, [projectId]);

    // first item is selected on page load
    useEffect(() => {
        handleSelectItem(0);
    }, [mergeRequests]);

    const classes = useStyles();
    return (
        <AuthView>
            <NavBar/>
            <NavTabs tabSelected={2}/>
            <Box m={1}/>
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
                                          handleSelectedItemChange={handleSelectItem}/>
                            </Box>
                        </Card>
                    </Grid>

                    <Grid item xs={12} md={8} lg={9}>
                        <Card>
                            <NotesList notes={notes}/>
                        </Card>
                    </Grid>
                </Grid>
            </Container>
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
    data: { items: Task[], selectedItem: number, setSelectedItem: React.Dispatch<number> },
    index: number,
    style: React.CSSProperties
}) => {

    const {items, selectedItem, setSelectedItem} = data;
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
                primary={item.title}
                secondary={`#${item.iid} · opened ${item.created_at} by ${item.author.name}`}/>
        </ListItem>
    );
}, areEqual);

const createItemData = memoize((items: Task[],
                                selectedItem: number,
                                setSelectedItem: React.Dispatch<number>) => ({
    items,
    selectedItem,
    setSelectedItem,
}));

const TaskList = ({items, selectedItem, handleSelectedItemChange}
                      : {
                      items: Task[],
                      selectedItem: number,
                      handleSelectedItemChange: React.Dispatch<number>
                  }
) => {

    const itemData = createItemData(items, selectedItem, handleSelectedItemChange);
    return (
        <AutoSizer>
            {({height, width}) => (
                <FixedSizeList
                    height={height}
                    itemCount={items.length}
                    itemData={itemData}
                    itemSize={100}
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
        <List subheader={<ListSubheader disableSticky>{`${notes.length} Notes`}</ListSubheader>}
              className={classes.notesList}
        >
            {notes.map((note) => (
                <ListItem key={note.id}>
                    <ListItemText
                        primary={
                            <React.Fragment>
                                {`${note.author.name} `}
                                <Typography
                                    component="span"
                                    variant="body2"
                                    color="textSecondary"
                                >
                                    {`@${note.author.username} · ${note.created_at}`}
                                </Typography>
                            </React.Fragment>}
                        secondary={note.body}/>
                </ListItem>
            ))}
        </List>
    );
};

export default NotesPage;
