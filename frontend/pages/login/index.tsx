import { Box, Button, Input, TextField } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import React from "react";

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
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
    >
      <Box
        className={classes.card}
        boxShadow={20}
        width="20vw"
        height="60vh"
        display="flex"
        justifyContent="center"
        borderRadius={16}
      >
        Test
        <Box className="cardPadding">
          <Box color="gray">
            <Input defaultValue="Username" disableUnderline />
          </Box>
          <Button variant="contained">Login</Button>
        </Box>
      </Box>
    </Box>
  );
};

export default index;
