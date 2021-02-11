import React from "react";
import Image from "next/image";
import axios, { AxiosResponse, AxiosError } from "axios";
import {Box, Typography} from "@material-ui/core";
import AppTextField from "./AppTextField";
import AppButton from "./AppButton";
import { useSnackbar } from 'notistack';

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
        <Box
            bgcolor="primary.contrastText"
            boxShadow={20}
            width="20vw"
            height="65vh"
            minWidth="250px"
            minHeight="400px"
            display="flex"
            flexDirection="column"
            justifyContent="center"
            alignItems="center"
            borderRadius={45}
            padding="20px"
        >
            <Image
                src="/gitlab.svg"
                alt="The Gitlab Logo"
                width={100}
                height={100}
            />
            <Typography variant="h6" align="center">GitLab<br/>Analyzer</Typography>
            <AppTextField placeholder="Username" value={userName} onChange={(e) => setUserName(e.target.value)}/>
            <AppTextField placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
            <AppButton color="primary" onClick={handleLogin}>Login</AppButton>
        </Box>

    )
}


export default LoginCard;
