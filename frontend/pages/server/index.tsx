import React, {useEffect, useState} from "react";
import axios, {AxiosResponse, AxiosError} from "axios";
import {List, ListItem, ListItemSecondaryAction, ListItemText, Link, Icon, Box} from "@material-ui/core";
import Button from '@material-ui/core/Button';
import {useSnackbar} from 'notistack';
import {useRouter} from "next/router";
import AuthView from "../../components/AuthView";
import {AuthContext} from "../../components/AuthContext";
import NextLink from 'next/link'
import {UserServerView} from "../../interfaces/UserServerView";
import CardLayout from "../../components/CardLayout";
import AppButton from "../../components/AppButton";
import EditIcon from '@material-ui/icons/Edit';
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles({
    itemName: {
        fontWeight: 500,
        color: "#333333"
    }
})

const Server = () => {
    const classes = useStyles();
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [isLoading, setIsLoading] = React.useState<boolean>(true);
    const [servers, setServers] = useState<UserServerView[]>([]);

    useEffect(() => {
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/servers`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                if (resp.data) {
                    const userServers:UserServerView[] = resp.data;
                    if (userServers.length === 0) {
                        router.push(`/server/add`)
                    }
                    else {
                        setServers(userServers);
                        setIsLoading(false);
                    }
                }
            }).catch(() => {
            enqueueSnackbar('Failed to get servers.', {variant: 'error',});
        });
    }, []);

    return (
        <AuthView>
            {!isLoading && <CardLayout size="md">
            <List>
                {servers.map((server) => {
                    return (
                    <ListItem key={server.serverId}>
                        <ListItemText
                            primary={server.serverUrl}
                            classes={{primary: classes.itemName}}
                        />
                        <ListItemSecondaryAction>
                            <NextLink href={`/server/${server.serverId}/projects`} passHref>
                                <AppButton size="medium" color="primary">Select</AppButton>
                            </NextLink>
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
            </CardLayout>}
        </AuthView>
    );
}

export default Server;
