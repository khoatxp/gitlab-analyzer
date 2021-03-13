import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import {MergeRequest} from "../../interfaces/GitLabMergeRequest";
import {Box, Typography} from "@material-ui/core";


type MergeRequestListProps = {
    mergeRequests: MergeRequest[]
    handleSelectMergeRequest: (mergeRequest: MergeRequest) => void;
}
const MergeRequestList = ( {mergeRequests, handleSelectMergeRequest}: MergeRequestListProps) => {
    return (
        <Box border={1} borderColor={'lightGray'} mb={1}>
            <Typography variant="h3" align="center">Merge requests: </Typography>
            <List component="nav">
                {
                    mergeRequests.map((mergeRequest) => (
                        <ListItem
                            key={`${mergeRequest.id}-${mergeRequest.iid}`}
                            button
                            onClick={() => handleSelectMergeRequest(mergeRequest)}
                        >
                            <ListItemText primary={mergeRequest.title} />
                        </ListItem>
                    ))
                }
            </List>
        </Box>
    );
}

export default MergeRequestList;