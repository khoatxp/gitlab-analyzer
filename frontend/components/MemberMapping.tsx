import React, {useEffect} from "react";
import {useRouter} from "next/router";
import {AuthContext } from "./AuthContext";
import axios, {AxiosError, AxiosResponse} from "axios";
import {CommitAuthor} from "../interfaces/CommitAuthor";
import {GitManagementUser} from "../interfaces/GitManagementUser";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import {
    FormControl, Icon,
    InputLabel, Link,
    Select, Typography
} from "@material-ui/core";
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import TableFooter from '@material-ui/core/TableFooter';
import Paper from '@material-ui/core/Paper';
import MenuItem from '@material-ui/core/MenuItem';
import AppButton from "./app/AppButton";
import {useSnackbar} from "notistack";
import NextLink from "next/link";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        tableContainer:{
            margin:'50px 150px',
            borderRadius: "2%",
        },
        dropdownStyle: {
            border: "1px white",
            borderRadius: "5%",
            backgroundColor:'#E5E4E2',
        },
        tableHead:{
            backgroundColor: '#E5E4E2',
        },
        authorNameText: {
            fontSize: '1.5em',
            margin: '16px 0px',
        },
        smallTextColor: {
            color: 'grey',
            margin: '0px 0px',
        },
        formControl: {
            margin: theme.spacing(1),
            minWidth: 120,
        },
        selectEmpty: {
            marginTop: theme.spacing(2),
        },
        unmappedAuthorText:{
            color: 'red'
        },
        linkBack: {
            display: 'flex',
            alignItems: 'center',
            flexWrap: 'wrap'
        }
    }),
);

const MemberMapping = () => {
    const classes = useStyles();
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const { projectId, startDateTime, endDateTime, scoreProfileId } =  router.query;
    const dateQuery = `?startDateTime=${startDateTime}&endDateTime=${endDateTime}`;
    const [commitAuthors, setCommitAuthors] = React.useState<CommitAuthor[]>([]);
    const [gitManagementUsers, setGitManagementUsers] = React.useState<GitManagementUser[]>([]);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/${projectId}/commits/authors`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setCommitAuthors(resp.data);
                }).catch((err: AxiosError)=>{
                enqueueSnackbar(`Failed to get commit authors: ${err.message}`, {variant: 'error',});
            })
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/${projectId}/managementusers/members`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setGitManagementUsers(resp.data);
                }).catch((err: AxiosError)=>{
                enqueueSnackbar(`Failed to get members: ${err.message}`, {variant: 'error',});
            })
        }
    }, [projectId]);

    const handleMemberChange = (event: any, i:number) => {
        let items = [...commitAuthors];
        let item = {...items[i]}
        item.mappedGitManagementUserId = event.target.value?event.target.value[0]:item.mappedGitManagementUserId;
        item.mappedGitManagementUserName =  event.target.value?event.target.value[1]:item.mappedGitManagementUserName;
        items[i] = item;
        setCommitAuthors(items);
    };

    const handleSave = () => {
        axios
            .post(`${process.env.NEXT_PUBLIC_API_URL}/${projectId}/commits/mapping`,commitAuthors,getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                enqueueSnackbar("Saved changes successfully!", {variant: 'success',});
            }).catch((err: AxiosError) => {
            enqueueSnackbar(`Failed to save changes: ${err.message}`, {variant: 'error',});
        })
    }

    return(
        <TableContainer className={classes.tableContainer} component={Paper}>
            <Table>
                <TableHead className={classes.tableHead}>
                    <TableRow>
                        <TableCell>Commit Author</TableCell>
                        <TableCell>Mapped Gitlab Member</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {commitAuthors.map((commitAuthor,i) => (
                        <TableRow key={i}>
                            <TableCell>
                                <h2 className={classes.authorNameText}>{`${commitAuthor.authorName}`}</h2>
                                <p className={classes.smallTextColor}>{`${commitAuthor.authorEmail}`}</p>
                            </TableCell>
                            <TableCell>
                                <FormControl className={classes.formControl}>
                                    <InputLabel htmlFor="member">{commitAuthor.mappedGitManagementUserName?"Member":""}</InputLabel>
                                    <Select
                                        labelId="member-select-label"
                                        id="member-select"
                                        displayEmpty
                                        value={commitAuthor.mappedGitManagementUserName}
                                        MenuProps={{ classes: { paper: classes.dropdownStyle } }}
                                        onChange={event => handleMemberChange(event, i)}
                                    >
                                        <MenuItem disabled value={commitAuthor.mappedGitManagementUserName}>
                                            <em className={commitAuthor.mappedGitManagementUserName?'':classes.unmappedAuthorText}>
                                                {commitAuthor.mappedGitManagementUserName?commitAuthor.mappedGitManagementUserName:"Unmapped"}
                                            </em>
                                        </MenuItem>
                                        {gitManagementUsers.map(gitManagementUser =>(
                                            <MenuItem key={gitManagementUser.id} value={[gitManagementUser.id.toString(),gitManagementUser.name]}>
                                                {gitManagementUser.name}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
                <TableFooter>
                    <TableRow>
                        <TableCell>
                            <NextLink href={`/project/${projectId}/0/overview${dateQuery}&scoreProfileId=${scoreProfileId}`} passHref>
                                <Link classes={{root:classes.linkBack}}>
                                    <Icon fontSize="small">arrow_back</Icon>
                                    <Typography variant="button"> BACK</Typography>
                                </Link>
                            </NextLink>
                        </TableCell>
                        <TableCell>
                            <AppButton color="primary" onClick={handleSave}>Save changes</AppButton>
                        </TableCell>
                    </TableRow>
                </TableFooter>
            </Table>
        </TableContainer>
    )
}

export default MemberMapping;