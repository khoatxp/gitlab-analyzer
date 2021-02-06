import { Box } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import React from "react";
import LoginCard from "../../components/LoginCard";

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
      <LoginCard/>
    </Box>
  );
};

export default index;
