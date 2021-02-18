import React from "react";
import Box from '@material-ui/core/Box';
import CardLayout from "../../components/CardLayout";
import AppButton from "../../components/AppButton";
import styles from "../../components/Progress.module.css";
import AppProgressBar from "../../components/AppProgressBar";

const index = () => {
    const [progress, setProgress] = React.useState<number>(0);
    React.useEffect(() => {
        // TODO: Below is just dummy progress logic. Update once we can actually fetch project data.
        const timer = setInterval(() => {
            setProgress(oldProgress => {
                return Math.min(oldProgress + 5, 100);
            });
        }, 400);
        return () => {
            clearInterval(timer);
        };
    }, []);

    return (
        <CardLayout>
            <AppProgressBar variant="determinate" value={progress}/>
            <div className={styles.loading__container}>
                <p className={`${styles.loading__done} ${progress === 100 && styles.loading__appeared}`}>Importing
                    done!</p>
                <p className={`${styles.loading__dots} ${progress === 100 && styles.loading__disappeared}`}>Importing
                    commits</p>
                <p className={`${styles.loading__dots} ${progress === 100 && styles.loading__disappeared}`}>Importing
                    merge requests</p>
                <p className={`${styles.loading__dots} ${progress === 100 && styles.loading__disappeared}`}>Importing
                    comments</p>
            </div>
            <Box textAlign="center">
                <AppButton color='primary'> Cancel </AppButton>
            </Box>
        </CardLayout>
    );
};

export default index;
