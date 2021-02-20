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

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        root: {},
        card: {
            padding: theme.spacing(1),
            overflow: 'auto',
            height: '75vh',
        },
        appBarSpacer: theme.mixins.toolbar,
        content: {
            flexGrow: 1,
        },
        container: {
            paddingTop: theme.spacing(2),
            paddingBottom: theme.spacing(2),
        },
        sticky: {},
        overflow: {
            overflow: 'auto',
            height: '80vh',
        },
        inline: {
            display: 'inline',
        },
    }),
);

interface Note {
    id: number;
    name: string;
    email: string;
    body: string;
}

enum NoteType {
    MergeRequest,
    Issue
}

// const URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/5/merge_request/10/notes`
const URL = "https://jsonplaceholder.typicode.com/comments";

const index = () => {
    const [notes, setNotes] = useState<Note[]>([]);
    const [mergeRequests, setMergeRequests] = useState<Note[]>([]);

    const [selectedItem, setSelectedItem] = useState(0);
    const handleSelectItem = (
        index: number,
    ) => {
        axios
            .get(`${URL}?postId=${selectedItem + 1}`)
            .then((resp: AxiosResponse) => {
                setNotes(resp.data);
            });
        setSelectedItem(index);
    };

    const [noteType, setNoteType] = React.useState(NoteType.MergeRequest);
    const handleSelectNoteType = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNoteType(Number((event.target as HTMLInputElement).value));
    };


    useEffect(() => {
        axios
            .get(URL)
            .then((resp: AxiosResponse) => {
                setNotes(resp.data);
                setMergeRequests(resp.data);
            });
    }, []);


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
                                <MergeRequestList items={mergeRequests}
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
    data: { items: Note[], selectedItem: number, setSelectedItem: React.Dispatch<number> },
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
                primary={"21-Pull-repo-Information-from-gitlab-server"}
                secondary={"!11 · opened 1 day ago by Jason Lee"}/>
        </ListItem>
    );
}, areEqual);

const createItemData = memoize((items: Note[],
                                selectedItem: number,
                                setSelectedItem: React.Dispatch<number>) => ({
    items,
    selectedItem,
    setSelectedItem,
}));

function MergeRequestList({items, selectedItem, handleSelectedItemChange}
                              : {
                              items: Note[],
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
            {notes.map((note) => (
                <ListItem>
                    <ListItemText
                        primary={
                            <React.Fragment>
                                {"First Last "}
                                <Typography
                                    component="span"
                                    variant="body2"
                                    className={classes.inline}
                                    color="textSecondary"
                                >
                                    {"@flast · 6 hours ago"}
                                </Typography>
                            </React.Fragment>}
                        secondary={note.body}/>
                </ListItem>
            ))}
        </List>
    );
}

export default index;
