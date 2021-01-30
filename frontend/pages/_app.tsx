import Head from 'next/head'
import { useEffect } from "react";
import { ThemeProvider } from "@material-ui/core/styles";
import CssBaseline from '@material-ui/core/CssBaseline';
import theme from "../styles/theme";
import "../styles/globals.css";
import NavBar  from '../components/NavBar';
import type { AppProps } from 'next/app'

function MyApp({ Component, pageProps } : AppProps) {
  // Remove the server-side injected CSS.
  // This is done for Material UI
  useEffect(() => {
    const jssStyles = document.querySelector("#jss-server-side");
    if (jssStyles && jssStyles.parentElement) {
      jssStyles.parentElement.removeChild(jssStyles);
    }
  }, []);

  return (
    <>
      <Head>
        <title>Create Next App</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <ThemeProvider theme={theme}>
        {/* CssBaseline kickstart an elegant, consistent, and simple baseline to build upon. */}
        <CssBaseline />
        <NavBar/>
        <Component {...pageProps} />
      </ThemeProvider>
    </>
  );
}

export default MyApp;
