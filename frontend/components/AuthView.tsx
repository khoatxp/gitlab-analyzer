import React from "react";
import {AuthContext} from "./AuthContext";
import {useRouter} from "next/router";
import Box from "@material-ui/core/Box";
import {CircularProgress} from "@material-ui/core";
import ChildrenProps from "../interfaces/ChildrenProps";
import axios from "axios";

export default function AuthView({children}: ChildrenProps) {
    const {userCredential, getAxiosAuthConfig} = React.useContext(AuthContext);
    const [isLoading, setIsLoading] = React.useState(true);
    const router = useRouter();

    const isUserCredentialsAvailable = () => {
        return userCredential.username != '' && userCredential.password != '';
    }

    const isUserLoggedIn = (): Boolean => {
        // Make correct api call to check if user is actually logged in
        // TODO: Callout to SSO
        axios.get(
            `${process.env.NEXT_PUBLIC_API_URL}/login`, getAxiosAuthConfig()
        ).then(() => {
            return true;
        }).catch(() => {
            return false;
        })

        return false;
    }

    React.useEffect(() => {
        if (isUserCredentialsAvailable && isUserLoggedIn()) {
            setIsLoading(false);
        } else {
            router.push('/login').then(() => {
                console.log("Redirected user to login");
            });
        }
    }, [userCredential])

    return isLoading ? <LoadingPage/> : <>{children}</>
}

const LoadingPage = () => {
    return (
        <Box
            width="100vw"
            height="100vh"
            display="flex"
            alignItems="center"
            justifyContent="center"
        >
            <CircularProgress color="primary" size="10em"/>
        </Box>
    );
}