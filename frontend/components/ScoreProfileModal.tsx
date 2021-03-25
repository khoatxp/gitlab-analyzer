import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import ScoreProfile from "../interfaces/ScoreProfile";
import { makeStyles } from '@material-ui/core/styles';
import Box from '@material-ui/core/Box';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from "@material-ui/core/DialogContent";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from '@material-ui/core/DialogTitle';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import {AuthContext} from "./AuthContext";
import AppTextField from "./app/AppTextField";
import AppButton from "./app/AppButton";
import {useSnackbar} from 'notistack';



const useStyles = makeStyles((theme) => ({
    popup:{
        borderRadius:45,
        padding:"20px",
        boxShadow:'none',
    },
}));

interface Props {
    open : boolean
    handleClose : () => void
    id : number
    profile : ScoreProfile | null
    isNewProfile : boolean
    update: () => void
}


const ScoreProfileModal = ({ open,handleClose,id,profile,isNewProfile,update }: Props) => {
    const classes = useStyles();
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);


    const [savedArray, setSavedArray] = useState<{}>({});
    const [extensions, setExtensions] = useState([]);
    const [syntaxWeight, setSyntaxWeight] = useState<number>()
    const [commentsWeight, setCommentsWeight] = useState<number>();
    const [name, setName] = useState<string>()
    const [lineWeight, setLineWeight] = useState<number>();
    const [deleteWeight, setDeleteWeight] = useState<number>();
    const [map, setMap] = useState(new Map());

    useEffect(() => {

        if(isNewProfile == false && profile != null){
             setName(profile.name);
             setCommentsWeight(profile.commentsWeight);
             setDeleteWeight(profile.deleteWeight);
             setLineWeight(profile.lineWeight);
             setSyntaxWeight(profile.syntaxWeight);
             setExtensions([]);
             setMap(new Map(Object.entries(profile.extensionWeights)));
             for (let [key, value] of map) {
                setExtensions([...extensions, {extension: key, weight: value}]);
            }
        }
        else{
            setName("");
            setCommentsWeight(undefined);
            setDeleteWeight(undefined);
            setLineWeight(undefined);
            setSyntaxWeight(undefined);
            setExtensions([]);   
        }

    },[open])

    useEffect(() => {

        setSavedArray();
        extensions.map((file, index : number) => {
            setSavedArray({...savedArray, [file.extension]: file.weight});
        });

    }, [extensions])


    const close = () => {
        setSavedArray({});
        handleClose();
    };

    const handleAddExtension = () => {
        setExtensions([...extensions, {exension: "", weight: ""}]);
    };

    const handleRemoveExtension = (index : number) => {
        const list = [...extensions];
        list.splice(index, 1);
        setExtensions(list);
    };

    const handleExtensionChange = (extension : string , index : number ) => {
        const list = [...extensions];
        list[index].extension = extension;
        setExtensions(list);
    };

    const handleWeightChange = (weight : string, index : number) => {
        const list = [...extensions];
        list[index].weight = weight;
        setExtensions(list);

    }


    const handleSave = () => {

        if(name==""){
            enqueueSnackbar('Profile must have a name', {variant: 'error',});
            return;
        }
        if(lineWeight != undefined && lineWeight < 0 || commentsWeight != undefined && commentsWeight < 0 || deleteWeight != undefined && deleteWeight < 0 || syntaxWeight != undefined && syntaxWeight < 0){
            enqueueSnackbar('Weights cannot be negative', {variant: 'error',});
            return;
        }
        if(lineWeight == undefined || commentsWeight == undefined || deleteWeight == undefined || syntaxWeight == undefined){
            enqueueSnackbar('Text fields must not be empty', {variant: 'error',});
            return;
        }
        extensions.map((file, index) => {
            if (file.weight < 0 || file.weight == ""){
                enqueueSnackbar('Extension weights cannot be empty or negative', {variant: 'error',});
                return;
            }
            if (file.extension == ""){
                enqueueSnackbar('Extension names must not be empty', {variant: 'error',});
                return;
            }
        });    

        if (router.isReady) {

            const newProfile = {
                name: name,
                lineWeight: lineWeight,
                deleteWeight: deleteWeight,
                syntaxWeight: syntaxWeight,
                commentsWeight: commentsWeight,
                extensionWeights: savedArray,
            }

            if (isNewProfile == false) {
                axios
                .put(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile/${id}` , newProfile, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    enqueueSnackbar('Successfully saved score profile', {variant: 'success',});
                    update();
                }).catch(() => {
                    enqueueSnackbar('Failed to save score profile', {variant: 'error',});
                })
                close();
            } else {
                axios
                .post(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile` , newProfile, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    enqueueSnackbar('Successfully saved score profile', {variant: 'success',});
                    update();
                }).catch(() => {
                    enqueueSnackbar('Failed to save score profile', {variant: 'error',});
                })
                close();
            }
        }

    };


    return (

        <React.Fragment>
            <Dialog open={open} onClose={close} fullWidth maxWidth="sm" classes={{paper: classes.popup}} >
                <DialogTitle id="edit-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>{"Score Profile"}</DialogTitle>
                <DialogContent>
                    <form onSubmit={handleSave}>
                        <div style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>
                            <Box width={150}>
                                <AppTextField label="Name" value={name || ""} onChange={(e) => setName( e.target.value)} required/>
                            </Box>
                        </div>
                        <Box display="flex" flexDirection="row" justifyContent="center" >
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="New Line" placeholder="Weight"
                                type="number"
                                value={lineWeight || ""}
                                onChange={(e) => setLineWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Deleting" placeholder="Weight"
                                type="number"
                                value={deleteWeight || ""}
                                onChange={(e) => setDeleteWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Syntax(e.g '}')" placeholder="Weight"
                                type="number"
                                value={syntaxWeight || ""}
                                onChange={(e) => setSyntaxWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Comments" placeholder="Weight"
                                type="number"
                                value={commentsWeight|| ""}
                                onChange={(e) => setCommentsWeight(Number(e.target.value))}
                                />
                            </Box>
                        </Box>
                        <DialogTitle id="extension-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>{"Extensions"}</DialogTitle>
                        <Box  style={{ display:"flex", flexDirection:"column", justifyContent:"center", alignItems:"center"}} >
                            {extensions && extensions.length > 0 ?
                            extensions.map((file, index) => {
                                return (

                                    <Box
                                        key={index}
                                        boxShadow={0}
                                        display="flex"
                                        marginRight={3}
                                        marginLeft={3}
                                        flexDirection="row"
                                        justifyContent="space-between"
                                        alignItems="center"
                                    >
                                        <Box marginLeft={1} marginRight={1}>
                                            
                                            <AppTextField label="extension"
                                            value={file.extension || ""}
                                            onChange={(e) => handleExtensionChange(e.target.value ,index)}
                                            />
                                        </Box>
                                        <Box marginLeft={1} marginRight={1}>
                                            
                                            <AppTextField label="weight"
                                            value={file.weight || ""}
                                            onChange={(e) => handleWeightChange(e.target.value, index) }
                                            type="number"
                                            />
                                        </Box>
                                        <div>

                                            <IconButton edge={false} aria-label="deleteextension" onClick={()=>handleRemoveExtension(index)}>
                                                <DeleteIcon style={{ fontSize: "25px", color:"grey" }} />
                                            </IconButton>
                                        </div>
                                    </Box>
                                );
                            }): "No extensions set for this profile"}
                        </Box>
                        <div style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>
                            <IconButton edge={false} aria-label="addextension" onClick={handleAddExtension}>
                                <AddCircleIcon style={{ fontSize: "30px", color: "green" }} />
                            </IconButton>
                        </div>
                    </form>
                </DialogContent>
                <DialogActions>
                    <div >
                    <AppButton size="large" type="submit" color="primary" onClick={handleSave}>Save</AppButton>
                    <AppButton size="large"  color="primary" onClick={close}>Cancel</AppButton>
                    </div>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    )
}

export default ScoreProfileModal;
