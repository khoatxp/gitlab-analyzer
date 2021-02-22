import React from "react";
import {AuthContext} from "./AuthContext";
import {useRouter} from "next/router";
import Box from "@material-ui/core/Box";
import {CircularProgress} from "@material-ui/core";
import ChildrenProps from "../interfaces/ChildrenProps";
import axios from "axios";

// Wrapper component for pages that needs authentication
// Displays a loading bar until authentication status is resolved
// If authenticated, will display children. Redirects to login page otherwise
export default function AuthView({children}: ChildrenProps) {
    const {userCredential, getAxiosAuthConfig} = React.useContext(AuthContext);
    const [isLoading, setIsLoading] = React.useState(true);
    const router = useRouter();

    // Async method that will return whether or not the user is logged in
    // Facilitates easy async use in useState
    const testUserLoggedIn = async () => {
        if (!isUserCredentialsAvailable()) {
            return false;
        }
        try {
            await testCanLoginOnServer();
            return true;
        } catch {
            return false;
        }
    }

    const isUserCredentialsAvailable = () => {
        return userCredential.username != '' && userCredential.password != '';
    }

    const testCanLoginOnServer = async () => {
        // Make correct api call to check if user is actually logged in
        // Throws error on request failure
        // TODO: Callout to SSO instead
        await axios.get(
            `${process.env.NEXT_PUBLIC_API_URL}/login`,
            getAxiosAuthConfig()
        )
    }

    React.useEffect(() => {
        testUserLoggedIn().then((isUserLoggedIn) => {
            if (isUserLoggedIn) {
                setIsLoading(false);
            } else {
                router.push('/login').then(() => {
                    console.log("Redirected user to login");
                });
            }
        });
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
            <CircularProgress color="primary" size="7em"/>
        </Box>
    );
}