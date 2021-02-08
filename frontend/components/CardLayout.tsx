import {Box, Typography} from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import React, {ReactNode} from "react";
import Image from "next/image";

type Props = {
  children: ReactNode
}

const useStyles = makeStyles({
  backgroundGradient: {
    background: "radial-gradient(90% 180% at 50% 50%, #FFFFFF 0%, #FCA326 65%)",
    height: "100vh",
    width: "100vw",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  card: {
    background: "white",
    width: "100vw",
    minWidth: "250px",
    minHeight: "400px",
    maxWidth: "800px",
    display: "flex",
    flexDirection: "column",
    borderRadius: "16px",
    padding: "25px",
    margin: "25px"
  },
  logo: {
    alignSelf: "center",
  }
});

const CardLayout = ({ children }:Props) => {
  const classes = useStyles();
  return (
    <Box className={classes.backgroundGradient}>
      <Box className={classes.card} boxShadow={20}>
        <Box className={classes.logo}>
          <Image
              src="/gitlab.svg"
              alt="The Gitlab Logo"
              width={100}
              height={100}
          />
          <Typography variant="h6" align="center" gutterBottom>
            GitLab
            <br />
            Analyzer
          </Typography>
        </Box>
        { children }
      </Box>
    </Box>
  );
};

export default CardLayout;
