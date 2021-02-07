import React from "react";
import Image from "next/image";
import axios, { AxiosResponse, AxiosError } from "axios";
import {Box, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import AppTextField from "./AppTextField";
import AppButton from "./AppButton";
import { useSnackbar } from 'notistack';

const useStyles = makeStyles({
    card: {
        background: "white",
    },
});

const LoginCard = () => {
    const classes = useStyles();
    const { enqueueSnackbar } = useSnackbar();
    const [userName, setUserName] = React.useState<string>("");
    const [password, setPassword] = React.useState<string>("");

    const handleLogin = () => {
        enqueueSnackbar("Attempting login.", {variant: 'default',});
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/login`)
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
            className={classes.card}
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
            <AppTextField placeholder="Username" value={userName} setValue={setUserName}/>
            <AppTextField placeholder="Password" value={password} setValue={setPassword}/>
            <AppButton color="primary" onClick={handleLogin}>Login</AppButton>
        </Box>

    )
}


export default LoginCard;
