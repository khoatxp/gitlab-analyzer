import React, {useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import {
    Box, Dialog, DialogActions, DialogContent, DialogContentText,
    DialogTitle,
    Icon,
    List,
    ListItem,
    ListItemSecondaryAction,
    ListItemText,
    Typography
} from "@material-ui/core";
import Button from '@material-ui/core/Button';
import {useSnackbar} from 'notistack';
import AuthView from "../../components/AuthView";
import {AuthContext} from "../../components/AuthContext";
import NextLink from 'next/link'
import {UserServerView} from "../../interfaces/UserServerView";
import CardLayout from "../../components/layout/CardLayout";
import AppButton from "../../components/app/AppButton";
import {makeStyles} from "@material-ui/styles";
import EditIcon from "@material-ui/icons/Edit";
import DeleteIcon from "@material-ui/icons/Delete";

const useStyles = makeStyles({
    itemName: {
        fontWeight: 500,
        color: "#333333"
    },
    deleteButton: {
        color: 'white',
        backgroundColor: '#ff4569',
        '&:hover': {
            backgroundColor: '#ff1744',
        },
    }
})

type DialogProps = {
    open: boolean
    handleClose: () => void
    handleConfirm: () => void
}

const RemoveConfirmationDialog = ({open, handleClose, handleConfirm}: DialogProps) => {

    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">{"Remove Server?"}</DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    Are you sure you want to remove the server?
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <AppButton onClick={handleClose} size="medium">
                    Cancel
                </AppButton>
                <AppButton onClick={handleConfirm} size="medium" color="primary" autoFocus>
                    Remove
                </AppButton>
            </DialogActions>
        </Dialog>
    );
}

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
                <List>
                    {servers.map((server) => {
                        return (
                            <ListItem key={server.serverId}>
                                <ListItemText
                                    primary={server.serverUrl}
                                    classes={{primary: classes.itemName}}
                                />
                                <ListItemSecondaryAction>
                                    <NextLink href={`/server/${server.serverId}/`} passHref>
                                        <AppButton size="medium" color="primary">Select</AppButton>
                                    </NextLink>
                                    <NextLink href={`/server/${server.serverId}/edit`} passHref>
                                        <AppButton size="medium" startIcon={<EditIcon />}>Edit</AppButton>
                                    </NextLink>
                                    <AppButton size="medium" classes={{ root: classes.deleteButton }} startIcon={<DeleteIcon />} onClick={() => openRemoveConfirmation(server.serverId)}>Remove</AppButton>
                                </ListItemSecondaryAction>
                            </ListItem>);
                    })}
                </List>
                <Box textAlign="center" marginTop="10px">
                    <NextLink href={`/server/add`} passHref>
                        <Button>
                            <Icon>add_circle</Icon> Add New Server
                        </Button>
                    </NextLink>
                </Box>
                <RemoveConfirmationDialog open={dialogOpen} handleClose={closeRemoveConfirmation} handleConfirm={removeServer}/>
            </CardLayout>
            }
        </AuthView>
    );
}

export default Server;
