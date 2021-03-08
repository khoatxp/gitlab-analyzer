import React from "react";
import axios, {AxiosResponse, AxiosError} from "axios";
import AppTextField from "../../components/app/AppTextField";
import AppButton from "../../components/app/AppButton";
import CardLayout from "../../components/layout/CardLayout";
import {Box} from "@material-ui/core";
import {useSnackbar} from 'notistack';
import {useRouter} from "next/router";
import {AuthContext, UserCredential} from "../../components/AuthContext";

const Login = () => {
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const {setUserCredential} = React.useContext(AuthContext);
    const [username, setUserName] = React.useState<string>("");
    const [password, setPassword] = React.useState<string>("");

    const saveLoginSession = () => {
        // TODO: When changing to SSO. save token from response to local storage or cookie
        const credential: UserCredential = {
            username: username,
            password: password
        }
        setUserCredential(credential);
    }

    const handleLogin = () => {
        location.replace(`https://cas.sfu.ca/cas/login?service=http://localhost:8080/api/v1/auth`)
    }

    return (
        <CardLayout size="sm">
            <Box alignSelf="center">
                <AppButton color="primary" onClick={handleLogin}>Login</AppButton>
            </Box>
        </CardLayout>
    )
}


export default Login;
