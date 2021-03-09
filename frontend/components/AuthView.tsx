import React from "react";
import {AuthContext} from "./AuthContext";
import {useRouter} from "next/router";
import Box from "@material-ui/core/Box";
import {CircularProgress} from "@material-ui/core";
import ChildrenProps from "../interfaces/ChildrenProps";
import axios, {AxiosResponse} from "axios";

// Wrapper component for pages that needs authentication
// Displays a loading bar until authentication status is resolved
// If authenticated, will display children. Redirects to login page otherwise
export default function AuthView({children}: ChildrenProps) {
    const {user, setUser, getAxiosAuthConfig} = React.useContext(AuthContext);
    const [isLoading, setIsLoading] = React.useState(true);
    const router = useRouter();

    React.useEffect(() => {
        axios.get(
            `${process.env.NEXT_PUBLIC_API_URL}/user`,
            getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setUser(resp.data);
                setIsLoading(false);
            }).catch(() => {
            router.push('/login').then(() => {
                setUser(null);
                console.log("Redirected user to login");
            });
        });
    }, [])

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