import React, {useEffect, useState} from "react";
import axios, {AxiosResponse, AxiosError} from "axios";
import {List, ListItem, ListItemSecondaryAction, ListItemText} from "@material-ui/core";
import {useSnackbar} from 'notistack';
import {useRouter} from "next/router";
import AuthView from "../../components/AuthView";
import {AuthContext} from "../../components/AuthContext";
import Link from 'next/link'
import {UserServerView} from "../../interfaces/UserServerView";
import CardLayout from "../../components/CardLayout";

const Server = () => {
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
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
                    else if (userServers.length === 1) {
                        router.push(`/server/${userServers[0].serverId}/projects`)
                    }
                    else {
                        setServers(userServers);
                    }
                }
            }).catch(() => {
            enqueueSnackbar('Failed to get servers.', {variant: 'error',});
        });
    }, []);

    return (
        <AuthView>
            <CardLayout size="md">
            <List>
                {servers.map((server) => {
                    return (
                    <ListItem key={server.serverId}>
                        <ListItemText
                            primary={server.serverUrl}
                        />
                        <ListItemSecondaryAction>
                            <Link href={`/server/${server.serverId}/projects`}>
                                <a>Go</a>
                            </Link>
                        </ListItemSecondaryAction>
                    </ListItem>);
                })}
            </List>
            </CardLayout>
        </AuthView>
    );
}

export default Server;
