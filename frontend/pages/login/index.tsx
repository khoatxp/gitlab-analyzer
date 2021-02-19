import React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import AppTextField from "../../components/AppTextField";
import AppButton from "../../components/AppButton";
import { useSnackbar } from 'notistack';
import CardLayout from "../../components/CardLayout";
import {Box} from "@material-ui/core";

const Login = () => {
    const { enqueueSnackbar } = useSnackbar();
    const [userName, setUserName] = React.useState<string>("");
    const [password, setPassword] = React.useState<string>("");

    const handleLogin = () => {
        enqueueSnackbar("Attempting login.", {variant: 'default',});
        axios
            .post(`http://localhost:8080/login`,{},{
                auth: {
                    username: userName,
                    password: password,
                }
            })
            .then((resp: AxiosResponse) => {
                setUserName("");
                setPassword("");
                console.log(resp.data);
            })
            .catch((err: AxiosError) => {
                enqueueSnackbar(`Login failed: ${err.message}`, {variant: 'error',});
            })
    }

    return (
        <CardLayout size="sm">
            <AppTextField placeholder="Username" value={userName} onChange={(e) => setUserName(e.target.value)}/>
            <AppTextField placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
            <Box alignSelf="center">
                <AppButton color="primary" onClick={handleLogin}>Login</AppButton>
            </Box>
        </CardLayout>

    )
}


export default Login;
