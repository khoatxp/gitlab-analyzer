import React from "react";
import AppButton from "../../components/app/AppButton";
import CardLayout from "../../components/layout/CardLayout";
import {Box} from "@material-ui/core";

const Login = () => {
    const handleLogin = () => {
        location.replace(`https://cas.sfu.ca/cas/login?service=${process.env.NEXT_PUBLIC_BACKEND_URL}/login/cas`)
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
