import {Box, Typography} from "@material-ui/core";
import Image from "next/image";
import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import AppTextField from "./AppTextField";
import AppButton from "./AppButton";

const useStyles = makeStyles({
    card: {
        background: "white",
    },
});

const LoginCard = () => {
    const [userName, setUserName] = React.useState<string>("");
    const [password, setPassword] = React.useState<string>("");

    const classes = useStyles();
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
            padding="25px"
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
            <AppButton color="primary">Login</AppButton>
        </Box>

    )
}


export default LoginCard;
