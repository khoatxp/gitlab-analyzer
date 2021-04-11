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
    maxHeight: 440,
    minHeight: 440,
  },
});

const DiffItemList = ({diffItems, diffItemType, handleSelectDiffItem, selectedIndex, setSelectedIndex}: DiffItemListProps) => {
  const classes = useStyles();

  return (
    <Paper className={classes.root}>
        <TableContainer className={classes.container} style={{marginBottom: '1em'}}>
            <Table stickyHeader aria-label="sticky table">
                <TableHead>
                    <TableRow>
                        <TableCell
                          key="date"
                          align='left'
                          style={{ minWidth: 100 }}
                        >
                            Opened
                        </TableCell>
                        <TableCell
                          key="DiffItem"
                          align='left'
                          style={{ minWidth: 170 }}
                        >
                            {diffItems.length.toString()} {diffItemType}s
                        </TableCell>
                        <TableCell
                          key="CreatedBy"
                          align='left'
                          style={{ minWidth: 130 }}
                        >
                            Created By
                        </TableCell>
                        <TableCell
                          key="ID"
                          align='left'
                          style={{ minWidth: 100 }}
                        >
                            Issue #
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
                                    {formatDate(diffItem.createdAt)}
                                </TableCell>
                                <TableCell key="DiffItem" align='left' style={{ minWidth: 100 }}>
                                    {diffItem.title}
                                </TableCell>
                                <TableCell key="CreatedBy" align='left' style={{ minWidth: 100 }}>
                                    {diffItem.authorName}
                                </TableCell>
                                <TableCell key="ID" align='left' style={{ minWidth: 100 }}>
                                    #{diffItem.id}
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