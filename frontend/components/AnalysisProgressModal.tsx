import React, {useEffect, Fragment} from "react";
import { makeStyles } from '@material-ui/core/styles';
import {AuthContext} from "./AuthContext";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from "@material-ui/core/DialogContent";
// @ts-ignore
import SockJS from "sockjs-client";
// @ts-ignore
import Stomp from "stompjs";
import AppProgressBar from "./app/AppProgressBar";
import Box from "@material-ui/core/Box";
import AnimatedProgressText from "./AnimatedProgressText";
import {AnalysisRun} from "../interfaces/AnalysisRun";
import AppButton from "./app/AppButton";

const useStyles = makeStyles((theme) => ({
    popup:{
        borderRadius:45,
        padding:"20px",
        boxShadow:'none',
        width:"800px"
    },
}));


interface Props {
    open: boolean
    handleClose: () => void
    handleError: () => void
    handleWhenProgressIsDone: () => void
    analysisRun: AnalysisRun | null
}

const AnalysisProgressModal = ({open,handleClose,handleError,handleWhenProgressIsDone,analysisRun}: Props) =>{
    const classes = useStyles();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [progress, setProgress] = React.useState<number>(0);
    const [progressMessage, setProgressMessage] = React.useState<string>("Waiting for update");

    const handleCloseWhenDoneOrError = (progress:number, message: string) => {
        if(progress == 100){
            handleClose();
            handleWhenProgressIsDone();
        }
        if(message == "Error"){
            handleClose();
            handleError();
        }
    }

    useEffect(() => {
        const socket = new SockJS(`${process.env.NEXT_PUBLIC_BACKEND_URL}/websocket`);
        const stompClient = Stomp.over(socket);

        //turn off debugging logs on browser console
        stompClient.debug = () => {}

        if(analysisRun){
            stompClient.connect(getAxiosAuthConfig(), () => {
                stompClient.subscribe(`/topic/progress/${analysisRun.id}`, function (message:any) {
                    const body = JSON.parse(message.body);
                    setProgress(Number(body.progress));
                    setProgressMessage(body.message);
                    handleCloseWhenDoneOrError(Number(body.progress),body.message);
                });
            });
        }

        return ()=> {
            if(stompClient.connected){
                stompClient.disconnect();
            }
        }
    },[open])


    return(
        <Fragment>
            <Dialog open={open} classes={{paper: classes.popup}}>
                <DialogTitle id="edit-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>{"Progress"}</DialogTitle>
                <DialogContent>
                    <AppProgressBar variant="determinate" value={progress}/>
                    <Box margin="8px" display="flex" justifyContent="center" alignItems="center">
                        <AnimatedProgressText progress={progress}> {progressMessage} </AnimatedProgressText>
                    </Box>
                    <Box margin-top="10px" display="flex" justifyContent="center">
                        <AppButton color="primary" onClick={handleClose}>Close</AppButton>
                    </Box>
                </DialogContent>
            </Dialog>
        </Fragment>
    )
}

export default AnalysisProgressModal;