import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import formatDate from "../../utils/DateFormatter";
import {MergeRequest, OrphanCommitMergeRequest} from "../../interfaces/MergeRequest";
import {Commit} from "../../interfaces/Commit";

type DiffItemListProps = {
    diffItems: DiffItem[]
    diffItemType: string;
    handleSelectDiffItem: (diffItem: DiffItem) => void;
    selectedIndex: number;
    setSelectedIndex: (index: number) => any;
}

export type DiffItem = MergeRequest | Commit;

const useStyles = makeStyles({
  root: {
    width: '100%',
  },
  container: {
    height: 400,
  },
});

const DiffItemList = ({diffItems, diffItemType, handleSelectDiffItem, selectedIndex, setSelectedIndex}: DiffItemListProps) => {
    const classes = useStyles();

    return (
        <Paper className={classes.root}>
            <TableContainer className={classes.container} style={{marginBottom: '1em'}}>
                <Table stickyHeader aria-label="sticky table" >
                    <TableHead>
                        <TableRow>
                            <TableCell
                              key="date"
                              align='left'
                              style={{ minWidth: 100 }}
                            >
                                Date
                            </TableCell>
                            <TableCell
                              key="DiffItem"
                              align='left'
                              style={{ minWidth: 100 }}
                            >
                                {diffItems.length.toString()} {diffItemType}(s)
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            diffItems.map((diffItem, i) => (
                                <TableRow
                                    key={`${diffItem.id}`}
                                    hover
                                    onClick={() => {
                                        setSelectedIndex(i);
                                        handleSelectDiffItem(diffItem);
                                    }}
                                    selected={selectedIndex == i}
                                >
                                    <TableCell key="date" align='left' style={{ minWidth: 100 }}>
                                        {formatDate(diffItem.mergedAt || diffItem.createdAt)}
                                    </TableCell>
                                    <TableCell key="DiffItem" align='left' style={{ minWidth: 100 }}>
                                        {diffItem.title}
                                        {
                                            diffItem.id == OrphanCommitMergeRequest.id.toString() ?
                                            OrphanCommitMergeRequest.secondaryText : ` · ` +
                                            (diffItemType=='Merge Request' ? 'Merged ' : 'Committed ') +
                                            `by ${diffItem.authorUsername=='undefined' ? 'Unmapped User' : diffItem.authorUsername}`
                                        }
                                    </TableCell>
                                </TableRow>
                            ))
                        }
                    </TableBody>
                </Table>
            </TableContainer>
        </Paper>
    );
}

export default DiffItemList;