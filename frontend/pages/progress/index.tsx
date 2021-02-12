import React from "react";
import Image from "next/image";

import { Box, Button, TextField, Typography } from "@material-ui/core";
import { makeStyles,  createStyles, withStyles, Theme } from "@material-ui/core/styles";
import LinearProgress from '@material-ui/core/LinearProgress';

const BorderLinearProgress = withStyles((theme: Theme) =>
  createStyles({
    root: {
      height: 50,
      borderRadius: 50,
    },
    colorPrimary: {
      backgroundColor: theme.palette.grey[theme.palette.type === 'light' ? 200 : 700],
    },
    bar: {
      borderRadius: 25,
      backgroundColor: '#FCA326',
    },
  }),
)(LinearProgress);

const useStyles = makeStyles({
     backgroundGradient: {
        background: "radial-gradient(90% 180% at 50% 50%, #FFFFFF 0%, #FCA326 65%)",
     },
     card: {
        background: "white",
     },
     root: {
         width: '100%',
     },
});

const index = () => {
  const classes = useStyles();
  const [progress, setProgress] = React.useState(0);
    React.useEffect(() => {
        const timer = setInterval(() => {
            setProgress(oldProgress => {
              if (oldProgress === 100) {
                return 100;
              }
              return Math.min(oldProgress + 5, 100);
            });
        }, 400);
    return () => {
        clearInterval(timer);
        };
    }, []);

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
        width="60vw"
        height="45vh"
        minWidth="250px"
        minHeight="400px"
        display="flex"
        flexDirection="column"
        justifyContent="space-around"
        alignItems="center"
        borderRadius={16}
        padding="25px"
      >

        <Typography variant="h7" align="center">
            <Image
                src="/gitlab.svg"
                alt="The Gitlab Logo"
                width={75}
                height={75}
            />
            <br />
            GitLab
            <br />
            Analyzer
        </Typography>

        <div className={classes.root}>
           <BorderLinearProgress variant="determinate" value={progress} />
               <div className="loading__container">
                   <p className={`loading__done ${progress === 100 && "loading__appeared"}`}>Importing done!</p>
                   <p className={`loading_dots ${progress === 100 && "loading__disappeared"}`}>Importing commits</p>
                   <p className={`loading_dots ${progress === 100 && "loading__disappeared"}`}>Importing merge requests</p>
                   <p className={`loading_dots ${progress === 100 && "loading__disappeared"}`}>Importing comments</p>
               </div>
       </div>

        <Button variant="contained" color="primary" disableElevation>
          Cancel
        </Button>
      </Box>
    </Box>
  );
};

export default index;
