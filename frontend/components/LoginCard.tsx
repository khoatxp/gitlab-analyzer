import React from "react";
import axios, { AxiosResponse, AxiosError } from "axios";
import AppTextField from "./AppTextField";
import AppButton from "./AppButton";
import { useSnackbar } from 'notistack';
import CardLayout from "./CardLayout";
import {Box} from "@material-ui/core";

const LoginCard = () => {
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
                console.log(resp.data);
                setUserName("");
                setPassword("");
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


export default LoginCard;
