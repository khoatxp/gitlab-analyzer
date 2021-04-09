import React, {useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import {Box, Icon, IconButton, List, ListItem, ListItemSecondaryAction, ListItemText, Tooltip, Typography} from "@material-ui/core";
import Button from '@material-ui/core/Button';
import {useSnackbar} from 'notistack';
import AuthView from "../../components/AuthView";
import {AuthContext} from "../../components/AuthContext";
import NextLink from 'next/link'
import {UserServerView} from "../../interfaces/UserServerView";
import CardLayout from "../../components/layout/CardLayout";
import {makeStyles} from "@material-ui/styles";
import EditIcon from "@material-ui/icons/Edit";
import DeleteIcon from "@material-ui/icons/Delete";
import {PlayArrow} from "@material-ui/icons";
import ConfirmationDialog from "../../components/ConfirmationDialog";

const useStyles = makeStyles({
    listItemSecondaryAction: {
        paddingRight: 160,
    },
    itemName: {
        fontWeight: 500,
        color: "#333333",
        overflowWrap: 'break-word',
    },
    deleteButton: {
        color: '#ff4569',
    }
})

const Server = () => {
    const classes = useStyles();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [isLoading, setIsLoading] = React.useState<boolean>(true);
    const [servers, setServers] = useState<UserServerView[]>([]);
    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [serverIdToRemove, setServerIdToRemove] = useState<number>(0);

    useEffect(() => {
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/servers`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                if (resp.data) {
                    const userServers: UserServerView[] = resp.data;
                    setServers(userServers);
                    setIsLoading(false);
                }
            }).catch(() => {
            enqueueSnackbar('Failed to get servers.', {variant: 'error',});
        });
    }, []);

    const openRemoveConfirmation = (serverId:number) => {
        setServerIdToRemove(serverId);
        setDialogOpen(true);
    };

    const closeRemoveConfirmation  = () => {
        setDialogOpen(false);
    };

    const removeServer = () => {
        axios
            .delete(`${process.env.NEXT_PUBLIC_API_URL}/servers/${serverIdToRemove}`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setServerIdToRemove(0);
                setDialogOpen(false);
                let updatedServers = servers.filter(s => s.serverId !== serverIdToRemove);
                setServers(updatedServers);
            }).catch(() => {
            enqueueSnackbar('Failed to remove server.', {variant: 'error',});
        });
    }

    return (
        <AuthView>
            {!isLoading && <CardLayout size="md" logoType="header">
                <Typography align="center" variant="h5">Manage Servers</Typography>
                <Box maxHeight="50vh" overflow="auto">
                    <List>
                        {servers.map((server) => {
                            return (
                                <ListItem key={server.serverId} classes={{secondaryAction: classes.listItemSecondaryAction}}>
                                    <ListItemText
                                        primary={server.serverUrl}
                                        classes={{primary: classes.itemName}}
                                    />
                                    <ListItemSecondaryAction>
                                        <NextLink href={`/server/${server.serverId}/`} passHref>
                                            <Tooltip title="Select">
                                                <IconButton color="primary" aria-label="Select">
                                                    <PlayArrow />
                                                </IconButton>
                                            </Tooltip>
                                        </NextLink>
                                        <NextLink href={`/server/${server.serverId}/edit`} passHref>
                                            <Tooltip title="Edit">
                                                <IconButton aria-label="Edit">
                                                    <EditIcon />
                                                </IconButton>
                                            </Tooltip>
                                        </NextLink>
                                        <Tooltip title="Delete">
                                            <IconButton classes={{ root: classes.deleteButton }} aria-label="Delete" onClick={() => openRemoveConfirmation(server.serverId)}>
                                                <DeleteIcon />
                                            </IconButton>
                                        </Tooltip>
                                    </ListItemSecondaryAction>
                                </ListItem>);
                        })}
                    </List>
                </Box>
                <Box textAlign="center" marginTop="10px">
                    <NextLink href={`/server/add`} passHref>
                        <Button>
                            <Icon>add_circle</Icon> Add New Server
                        </Button>
                    </NextLink>
                </Box>
                <ConfirmationDialog
                    title="Remove Server?"
                    content="Are you sure you want to remove the server?"
                    confirmLabel="Remove"
                    open={dialogOpen}
                    handleClose={closeRemoveConfirmation}
                    handleConfirm={removeServer}/>
            </CardLayout>
            }
        </AuthView>
    );
}

export default Server;
