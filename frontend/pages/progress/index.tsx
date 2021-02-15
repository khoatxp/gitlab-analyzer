import React from "react";
import Image from "next/image";

import { Box, Button, TextField, Typography } from "@material-ui/core";
import { makeStyles,  createStyles, withStyles, Theme } from "@material-ui/core/styles";
import LinearProgress from '@material-ui/core/LinearProgress';
import AppGradientBackground from "../../components/AppGradientBackground";
import CardLayout from "../../components/CardLayout";
import AppButton from "../../components/AppButton";
import styles from "../../components/Progress.module.css";

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
      backgroundColor: 'secondary',
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
    <AppGradientBackground>
      <CardLayout>
        <div className={classes.root}>
           <BorderLinearProgress variant="determinate" value={progress} />
           <div className={styles.loading__container}>
               <p className={`${styles.loading__done} ${progress === 100 && styles.loading__appeared}`}>Importing done!</p>
               <p className={`${styles.loading__dots} ${progress === 100 && styles.loading__disappeared}`}>Importing commits</p>
               <p className={`${styles.loading__dots} ${progress === 100 && styles.loading__disappeared}`}>Importing merge requests</p>
               <p className={`${styles.loading__dots} ${progress === 100 && styles.loading__disappeared}`}>Importing comments</p>
           </div>
        </div>
        <AppButton color='primary'> Cancel </AppButton>
      </CardLayout>
    </AppGradientBackground>
  );
};

export default index;
