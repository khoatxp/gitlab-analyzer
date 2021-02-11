import {Box, Button, TextField, Typography} from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import React from "react";
import Image from "next/image";

const useStyles = makeStyles({
  backgroundGradient: {
    background: "radial-gradient(90% 180% at 50% 50%, #FFFFFF 0%, #FCA326 65%)",
  },
  card: {
    background: "white",
  },
});

const index = () => {
  const classes = useStyles();
  return (
    <Box
      className={classes.backgroundGradient}
      height="100vh"
      width="100vw"
      display="flex"
      justifyContent="center"
      alignItems="center"
      >
      <Box
        className={classes.card}
        boxShadow={20}
        width="20vw"
        height="60vh"
        minWidth="250px"
        minHeight="400px"
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        borderRadius={16}
        padding="25px"
      >
        <Image
          src="/gitlab.svg"
          alt="The Gitlab Logo"
          width={100}
          height={100}
        />
        <Typography variant="h6" align="center">
          GitLab
          <br />
          Analyzer
        </Typography>
        <TextField fullWidth placeholder="Username" variant="filled" />
        <TextField fullWidth placeholder="Password" variant="filled" />
        <TextField fullWidth placeholder="GitLab Token" variant="filled" />
        <Button variant="contained" color="primary" disableElevation>
          Login
        </Button>
      </Box>
    </Box>
  );
};

export default index;
