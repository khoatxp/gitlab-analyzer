import React, {useState} from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import {MergeRequest} from "../../interfaces/GitLabMergeRequest";
import {Avatar, Box, Card, Divider, ListItemIcon, ListSubheader} from "@material-ui/core";
import formatDate from "../../interfaces/dateFormatter";

type MergeRequestListProps = {
    mergeRequests: MergeRequest[]
    handleSelectMergeRequest: (mergeRequest: MergeRequest) => void;
}

const MergeRequestList = ({mergeRequests, handleSelectMergeRequest}: MergeRequestListProps) => {
    const [selectedIndex, setSelectedIndex] = useState(-1);

    return (
        <Card style={{'marginBottom': '1em'}}>
            <List
                component="nav"
                disablePadding
                subheader={
                    <ListSubheader>Merge Requests</ListSubheader>
                }
            >

                <Divider/>
                <Box height="34vh" overflow="auto">
                {
                    mergeRequests.map((mergeRequest, i) => (
                        <ListItem
                            key={`${mergeRequest.id}-${mergeRequest.iid}`}
                            button
                            divider={i != mergeRequests.length - 1} // Do not add a divider for the last item
                            onClick={() => {
                                setSelectedIndex(i);
                                handleSelectMergeRequest(mergeRequest);
                            }}
                            selected={selectedIndex == i}
                        >
                            <ListItemIcon>
                                <Avatar alt={`Author: ${mergeRequest.author.name}`}
                                        src={mergeRequest.author.avatar_url || ''}/>
                            </ListItemIcon>
                            <ListItemText
                                primary={mergeRequest.title}
                                secondary={`#${mergeRequest.iid} · opened ${formatDate(mergeRequest.created_at)} by ${mergeRequest.author.name}`}
                            />
                        </ListItem>
                    ))
                }
                </Box>
            </List>
        </Card>
    );
}

export default MergeRequestList;