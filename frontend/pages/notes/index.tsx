import { makeStyles } from "@material-ui/core/styles";
import React, {useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import NavBar from "../../components/NavBar";
import {Link} from "@material-ui/core";

const useStyles = makeStyles({
    table: {
        minWidth: 650,
    },
});

interface Note {
    id: number;
    name : string;
    email: string;
    body: string;
}

const URL = "https://jsonplaceholder.typicode.com/comments";

const index = () => {
    const [notes, setNotes] = useState<Array<Note>>([]);


    useEffect(() => {
        axios
            .get(URL)
            .then((resp: AxiosResponse) => {
                setNotes(resp.data);
            });
    }, []);

    return (
        <>
            <NavBar />
            <NotesTable notes={notes}/>
        </>
    );
};

interface NotesArray {
    notes: Note[];
}

const NotesTable: React.FC<NotesArray> = ({notes}) => {
    const classes = useStyles();

    {/* https://material-ui.com/components/tables/ */}
    return (
        <TableContainer component={Paper}>
            <Table className={classes.table} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell align="left">Date</TableCell>
                        <TableCell align="left">Author</TableCell>
                        <TableCell align="left">Comment</TableCell>
                        <TableCell align="left">Word Count</TableCell>
                        <TableCell align="left">Link</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {notes.map((note) => {
                        const noteUrl = `${URL}/${note.id}`;

                        return (
                            <TableRow key={note.id}>
                                <TableCell component="th" scope="row">
                                    {"Jan. 20, 2021"}
                                </TableCell>
                                <TableCell align="left">{`firstname lastname ${note.email}`}</TableCell>
                                <TableCell align="left">{note.body}</TableCell>
                                <TableCell align="left">{50}</TableCell>

                                <TableCell align="left">
                                    <Link href={noteUrl}>{noteUrl}</Link>
                                </TableCell>
                            </TableRow>
                        );
                    })}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default index;
