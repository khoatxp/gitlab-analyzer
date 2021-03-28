import React, {useEffect, Fragment} from "react";
import { makeStyles } from '@material-ui/core/styles';
import axios, {AxiosResponse} from "axios";
import {AuthContext} from "./AuthContext";
import {useSnackbar} from 'notistack';
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
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [progress, setProgress] = React.useState<number>(0);
    const [progressMessage, setProgressMessage] = React.useState<string>("Waiting for update");

    useEffect(() => {
        const socket = new SockJS(`${process.env.NEXT_PUBLIC_BACKEND_URL}/websocket`);
        const stompClient = Stomp.over(socket);

        //turn off debugging logs on browser console
        stompClient.debug = () => {}

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

        if(analysisRun){
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/analysis_run/progress/${analysisRun.id}`,getAxiosAuthConfig())
                .then((res: AxiosResponse) => {
                    setProgress(Number(res.data.progress));
                    setProgressMessage(res.data.message);
                    if(res.data.message){
                        handleCloseWhenDoneOrError(Number(res.data.progress),res.data.message);
                    }
                }).catch((err) => {
                enqueueSnackbar(`Failed to get projects analysis progress: ${err.message}`, {variant: 'error',});
            });

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
            <Dialog open={open} onClose={handleClose} classes={{paper: classes.popup}}>
                <DialogTitle id="edit-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>{"Progress"}</DialogTitle>
                <DialogContent>
                    <AppProgressBar variant="determinate" value={progress}/>
                    <Box margin="8px">
                        <AnimatedProgressText progress={progress}> {progressMessage} </AnimatedProgressText>
                    </Box>
                </DialogContent>
            </Dialog>
        </Fragment>
    )
}

export default AnalysisProgressModal;