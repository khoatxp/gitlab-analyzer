import React from "react";
import axios, {AxiosError, AxiosResponse} from "axios";
import AppTextField from "../../../components/app/AppTextField";
import AppButton from "../../../components/app/AppButton";
import CardLayout from "../../../components/layout/CardLayout";
import {Box, Typography} from "@material-ui/core";
import {useSnackbar} from 'notistack';
import {useRouter} from "next/router";
import AuthView from "../../../components/AuthView";
import {AuthContext} from "../../../components/AuthContext";

const EditServer = () => {
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const {serverId} = router.query;
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [serverAccessToken, setServerAccessToken] = React.useState<string>("");
    const [isValidAccessToken, setIsValidAccessToken] = React.useState<boolean>(true);

    const validateToken = (token:string) => {
        return token.length >= 20
    }
    const saveServer = () => {
        let trimmedAccessToken= serverAccessToken.trim();
        let isValidToken = validateToken(trimmedAccessToken);
        setIsValidAccessToken(isValidToken);

        if (isValidToken) {
            axios
                .put(`${process.env.NEXT_PUBLIC_API_URL}/servers/${serverId}`,
                     trimmedAccessToken,
                    getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    enqueueSnackbar('Access token saved', {variant: 'success',});
                    router.push(`/server`)
                }).catch((error: AxiosError) => {
                    let data = error.response?.data;
                    let errMsg = data && data.message ? data.message : 'Failed to save server access token.';
                    enqueueSnackbar(errMsg, {variant: 'error',});
            });
        }
    }

    return (
        <AuthView>
            <CardLayout size="md" backLink={"/server"} logoType="header">
                <Typography align="center"  variant="h5">Edit Server Access Token</Typography>
                <AppTextField
                    id="access-token"
                    placeholder="Access Token"
                    value={serverAccessToken}
                    error={!isValidAccessToken}
                    helperText={isValidAccessToken ? "" : "Token must be at least 20 characters"}
                    onChange={(e) => setServerAccessToken(e.target.value)}/>
                <Box alignSelf="center">
                    <AppButton id="save-server" color="primary" onClick={saveServer}>Save</AppButton>
                </Box>
            </CardLayout>
        </AuthView>
    )
}


export default EditServer;
