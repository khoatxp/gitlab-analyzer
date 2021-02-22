import Head from "next/head";
import {ThemeProvider} from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import theme from "../styles/theme";
import "../styles/globals.css";
import type {AppProps} from "next/app";
import {SnackbarOrigin, SnackbarProvider} from 'notistack';
import {AuthProvider} from "../components/AuthContext";

const snackBarAnchorOrigin: SnackbarOrigin = {
    vertical: 'top',
    horizontal: 'center',
}

function MyApp({Component, pageProps}: AppProps) {
    return (
        <>
            <Head>
                <title>Gitlab Analyzer</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            <ThemeProvider theme={theme}>
                {/* CssBaseline kickstart an elegant, consistent, and simple baseline to build upon. */}
                <CssBaseline/>
                <SnackbarProvider maxSnack={3} anchorOrigin={snackBarAnchorOrigin}>
                    <AuthProvider>
                        <Component {...pageProps} />
                    </AuthProvider>
                </SnackbarProvider>
            </ThemeProvider>
        </>
    );
}

export default MyApp;
