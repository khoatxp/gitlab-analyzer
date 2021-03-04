import React from "react";
import axios, {AxiosResponse} from "axios";
import AppTextField from "../../components/AppTextField";
import AppButton from "../../components/AppButton";
import CardLayout from "../../components/CardLayout";
import {Box} from "@material-ui/core";
import {useSnackbar} from 'notistack';
import {useRouter} from "next/router";
import AuthView from "../../components/AuthView";
import {AuthContext} from "../../components/AuthContext";

const AddServer = () => {
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [serverUrl, setServerUrl] = React.useState<string>("");
    const [serverAccessToken, setServerAccessToken] = React.useState<string>("");
    const [isValidServerUrl, setIsValidServerUrl] = React.useState<boolean>(true);
    const [isValidAccessToken, setIsValidAccessToken] = React.useState<boolean>(true);

    const validateUrl = (url:string) => {
        if (url && (url.startsWith("http://") || url.startsWith("https://"))){
            return true;
        }
        return false;
    }
    const validateToken = (token:string) => {
        if (token.length >= 20){
            return true;
        }
        return false;
    }
    const saveServer = () => {
        let trimmedUrl = serverUrl.trim();
        let isValidUrl = validateUrl(trimmedUrl);
        setIsValidServerUrl(isValidUrl);

        let trimmedAccessToken= serverAccessToken.trim();
        let isValidToken = validateToken(trimmedAccessToken);
        setIsValidAccessToken(isValidToken);

        if (isValidUrl && isValidToken) {
            axios
                .post(`${process.env.NEXT_PUBLIC_API_URL}/servers`,
                    {
                        serverUrl: trimmedUrl,
                        accessToken: trimmedAccessToken,
                        serverNickname: "nickname",
                    },
                    getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    router.push(`/server/${resp.data.serverId}/projects`)
                }).catch(() => {
                enqueueSnackbar('Failed to save server.', {variant: 'error',});
            });
        }
    }

    return (
        <AuthView>
            <CardLayout size="md">
                <AppTextField
                    placeholder="Server Url"
                    value={serverUrl}
                    error={!isValidServerUrl}
                    helperText={isValidServerUrl ? "" : "Url is missing scheme"}
                    onChange={(e) => setServerUrl(e.target.value)}/>
                <AppTextField
                    placeholder="Access Token"
                    value={serverAccessToken}
                    error={!isValidAccessToken}
                    helperText={isValidAccessToken ? "" : "Token must be at least 20 characters"}
                    onChange={(e) => setServerAccessToken(e.target.value)}/>
                <Box alignSelf="center">
                    <AppButton color="primary" onClick={saveServer}>Save</AppButton>
                </Box>
            </CardLayout>
        </AuthView>
    )
}


export default AddServer;
