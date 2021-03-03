import React from "react";
import Box from '@material-ui/core/Box';
import CardLayout from "../../components/layout/CardLayout";
import AppButton from "../../components/app/AppButton";
import AppProgressBar from "../../components/app/AppProgressBar";
import AnimatedProgressText from "../../components/AnimatedProgressText";

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
            <Box margin="8px">
                <AnimatedProgressText progress={progress}>Importing commits</AnimatedProgressText>
                <AnimatedProgressText progress={progress}>Importing merge requests</AnimatedProgressText>
                <AnimatedProgressText progress={progress}>Importing comments</AnimatedProgressText>
                { progress === 100 ? <p>Importing done!</p> : ''}
            </Box>
            <Box textAlign="center">
                <AppButton color='primary'> Cancel </AppButton>
            </Box>
        </CardLayout>
    );
};

export default index;
