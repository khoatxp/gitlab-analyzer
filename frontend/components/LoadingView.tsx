import React from "react";
import Box from "@material-ui/core/Box";
import {CircularProgress} from "@material-ui/core";

interface loadingViewProps {
    isRetrieving: boolean,
}

export default function LoadingView({isRetrieving}: loadingViewProps) {

    const [isLoading, setIsLoading] = React.useState(true);
    React.useEffect(() => {
        setIsLoading(isRetrieving)
    }, [isRetrieving]);

    return isLoading ? <LoadingPage/> : <></>
}

const LoadingPage = () => {
    return (
        <Box
            position="fixed"
            display="flex"
            top="50%"
            left="50%"
            zIndex="2"
            alignItems="center"
            justifyContent="center"
        >
            <CircularProgress color="primary" size="15em"/>
        </Box>
    );
}