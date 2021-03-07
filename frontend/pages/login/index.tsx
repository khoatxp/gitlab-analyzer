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
        saveLoginSession();
        axios.get(`${process.env.NEXT_PUBLIC_API_URL}/login`, {
            auth: {
                username: username,
                password: password,
            }
        }).then((resp: AxiosResponse) => {
            enqueueSnackbar("Login successful!", {variant: 'success',});
            router.push('/server/1/projects') // TODO: Change route so server id is not hard coded
        }).catch((err: AxiosError) => {
            enqueueSnackbar(`Login failed: ${err.message}`, {variant: 'error',});
        })
    }

    return (
        <CardLayout size="sm">
            <AppTextField placeholder="Username" value={username} onChange={(e) => setUserName(e.target.value)}/>
            <AppTextField placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
            <Box alignSelf="center">
                <AppButton color="primary" onClick={handleLogin}>Login</AppButton>
            </Box>
        </CardLayout>
    )
}


export default Login;
