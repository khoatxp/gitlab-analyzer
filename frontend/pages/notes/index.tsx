import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import React, {useEffect, useState, memo} from "react";
import axios, {AxiosResponse} from "axios";

import NavBar from "../../components/NavBar";
import {
    Box,
    Card,
    Container, FormControl, FormControlLabel,
    Grid,
    List,
    ListItem,
    ListItemText,
    ListSubheader, Radio, RadioGroup,
    Typography
} from "@material-ui/core";
import {FixedSizeList, areEqual} from 'react-window';
import memoize from 'memoize-one';
import AutoSizer from 'react-virtualized-auto-sizer';
import {Note} from "../../interfaces/GitLabNote";
import {MergeRequest} from "../../interfaces/GitLabMergeRequest";
import {Issue} from "../../interfaces/GitLabIssue";
import {Task} from "../../interfaces/GitLabTask";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        root: {},
        card: {
            padding: theme.spacing(1),
            overflow: 'auto',
            height: '75vh',
        },
        appBarSpacer: theme.mixins.toolbar,
        container: {
            paddingTop: theme.spacing(2),
            paddingBottom: theme.spacing(2),
        },
        overflow: {
            overflow: 'auto',
            height: '80vh',
        },
        inline: {
            display: 'inline',
        },
    }),
);

enum NoteType {
    MergeRequest,
    Issue
}

const PROJECT_ID = 5; // TODO: get project id from route
const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${PROJECT_ID}`;

const index = () => {
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
            .get(`${PROJECT_ID_URL}/merge_requests/${mergeRequestIid}/notes`)
            .then((resp: AxiosResponse) => {
                setNotes(resp.data);
            })
    };

    const getIssueNotes = (issueIid: number) => {
        axios
            .get(`${PROJECT_ID_URL}/issues/${issueIid}/notes`)
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
        axios
            .get(`${PROJECT_ID_URL}/merge_requests?startDateTime=2021-01-01T08:00:00Z&endDateTime=2021-03-15T08:00:00Z`)
            .then((resp: AxiosResponse) => {
                setMergeRequests(resp.data);
            });
        axios
            .get(`${PROJECT_ID_URL}/issues`)
            .then((resp: AxiosResponse) => {
                setIssues(resp.data);
            });

    }, []);

    // first item is selected on page load
    useEffect(() => {
        handleSelectItem(0);
    }, [mergeRequests]);

    const classes = useStyles();
    return (
        <Box className={classes.root}>
            <NavBar/>
            <div className={classes.appBarSpacer}/>
            <Container maxWidth='xl'>
                <Grid container spacing={2}>

                    <Grid item xs={12} md={4} lg={3}>
                        <Card>
                            <Box style={{display: 'flex', justifyContent: 'center'}}>
                                <RadioGroupSelectMergeRequestsOrIssues
                                    value={noteType}
                                    handleChange={handleSelectNoteType}/>
                            </Box>
                            <Box className={classes.card}>
                                <MergeRequestList items={noteType === NoteType.MergeRequest ? mergeRequests : issues}
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
        </Box>
    );
};

const RadioGroupSelectMergeRequestsOrIssues = ({value, handleChange}
                                                   : {
    value: NoteType,
    handleChange: React.Dispatch<React.ChangeEvent<HTMLInputElement>>
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
                    label="Merge Requests"
                />
                <FormControlLabel
                    value={NoteType.Issue}
                    control={<Radio color="secondary" size="small"/>}
                    label="Issues"
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

function MergeRequestList({items, selectedItem, handleSelectedItemChange}
                              : {
                              items: Task[],
                              selectedItem: number,
                              handleSelectedItemChange: React.Dispatch<number>
                          }
) {

    const itemData = createItemData(items, selectedItem, handleSelectedItemChange);
    const classes = useStyles();
    return (
        <AutoSizer>
            {({height, width}) => (
                <FixedSizeList
                    height={height}
                    itemCount={items.length}
                    itemData={itemData}
                    itemSize={100}
                    width={width}
                    className={classes.overflow}

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
        <List subheader={<ListSubheader disableSticky>Notes</ListSubheader>} className={classes.overflow}>
            {!notes?.length ? <Box>{"No notes!"}</Box> : notes.map((note) => (
                <ListItem>
                    <ListItemText
                        primary={
                            <React.Fragment>
                                {`${note.author.name} `}
                                <Typography
                                    component="span"
                                    variant="body2"
                                    className={classes.inline}
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

export default index;
