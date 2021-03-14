import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import {Avatar, Box, Card, Divider, ListItemIcon, ListSubheader} from "@material-ui/core";
import formatDate from "../../interfaces/dateFormatter";

type DiffItemListProps = {
    diffItems: DiffItem[]
    diffItemType: string;
    handleSelectDiffItem: (diffItem: DiffItem) => void;
    selectedIndex: number;
    setSelectedIndex: (index: number) => any;
}

export interface DiffItem {
    id: string;
    createdAt: string;
    authorName: string
    title: string;
    avatarUrl?: string;
}

// A dynamic list made to be able to handle both merge request and commit list rendering
const DiffItemList = ({diffItems, diffItemType, handleSelectDiffItem, selectedIndex, setSelectedIndex}: DiffItemListProps) => {

    return (
        <Card style={{marginBottom: '1em'}}>
            <List
                component="nav"
                disablePadding
                subheader={
                    <ListSubheader>{diffItems.length.toString()} {diffItemType}s</ListSubheader>
                }
            >
                <Divider/>
                <Box height="34vh" overflow="auto">

                    {
                        diffItems.map((diffItem, i) => (
                            <ListItem
                                key={`${diffItem.id}`}
                                button
                                divider={i != diffItems.length - 1} // Do not add a divider for the last item
                                onClick={() => {
                                    setSelectedIndex(i);
                                    handleSelectDiffItem(diffItem);
                                }}
                                selected={selectedIndex == i}
                            >
                                {
                                    // Display avatar if available
                                    diffItem.avatarUrl &&
                                    <ListItemIcon>
                                        <Avatar alt={`Author: ${diffItem.authorName}`}
                                                src={diffItem.avatarUrl || ''}/>
                                    </ListItemIcon>
                                }
                                <ListItemText
                                    primary={diffItem.title}
                                    secondary={`#${diffItem.id} · opened ${formatDate(diffItem.createdAt)} by ${diffItem.authorName}`}
                                />
                            </ListItem>
                        ))
                    }
                </Box>
            </List>
        </Card>
    );
}

export default DiffItemList;