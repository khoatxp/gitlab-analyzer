import React, {useState} from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import {MergeRequest} from "../../interfaces/GitLabMergeRequest";
import {Avatar, Card, Divider, ListItemIcon, ListSubheader} from "@material-ui/core";
import formatDate from "../../interfaces/dateFormatter";
import {Commit} from "../../interfaces/GitLabCommit";

type CommitListProps = {
    commits: Commit[]
    handleSelectCommit: (commit: Commit) => void;
}

const CommitList = ({commits, handleSelectCommit}: CommitListProps) => {
    const [selectedIndex, setSelectedIndex] = useState(-1);

    return (
        <Card>
            <List
                component="nav"
                disablePadding
                subheader={
                    <ListSubheader>Commits</ListSubheader>
                }
            >
                <Divider/>
                {
                    commits.map((commit, i) => (
                        <ListItem
                            key={`${commit.id}`}
                            button
                            divider={i != commits.length - 1} // Do not add a divider for the last item
                            onClick={() => {
                                setSelectedIndex(i);
                                handleSelectCommit(commit);
                            }}
                            selected={selectedIndex == i}
                        >
                            <ListItemText
                                primary={commit.title}
                                secondary={`#${commit.id} · opened ${formatDate(commit.created_at)} by ${commit.author_name}`}
                            />
                        </ListItem>
                    ))
                }
            </List>
        </Card>
    );
}

export default CommitList;