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
}


const ScoreProfileModal = ({ open,handleClose,id,profile,isNewProfile }: Props) => {
    const classes = useStyles();
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);


    const[savedArray, setSavedArray] = useState<{}>({});
    const [extensionMap, setExtensionMap] = useState(new Map())
    const [syntaxWeight, setSyntaxWeight] = useState<number | null>()
    const [commentsWeight, setCommentsWeight] = useState<number | null>();
    const [name, setName] = useState<string>()
    const [lineWeight, setLineWeight] = useState<number | null>();
    const [deleteWeight, setDeleteWeight] = useState<number | null>();

    useEffect(() => {

        if(isNewProfile == false && profile != null){
             setName(profile.name);
             setCommentsWeight(profile.commentsWeight);
             setDeleteWeight(profile.deleteWeight);
             setLineWeight(profile.lineWeight);
             setSyntaxWeight(profile.syntaxWeight);
             let map = new Map(Object.entries(profile.extensionWeights));
             setExtensionMap(map);
        }
        else{
            setName("");
            setCommentsWeight(null);
            setDeleteWeight(null);
            setLineWeight(null);
            setSyntaxWeight(null);
            let map= new Map();
            setExtensionMap(map);
        }

    },[open])

    useEffect(() => {

        setSavedArray({});
        Array.from(extensionMap).map((x, index) => {
            setSavedArray({...savedArray, [x[0]]:x[1]});
        })

    }, [extensionMap])


    const close = () => {
        setSavedArray({});
        handleClose();
    };

    const handleAddExtension = () => {
        setExtensionMap(prev => new Map([...prev, ["", ""]]))
    };

    const handleRemoveExtension = (extension : string) => {
        setExtensionMap((prev) => {
            const newMap = new Map(prev);
            newMap.delete(extension);
            return newMap;
        });
    };

    const handleExtensionChange = (oldExtension : string , newExtension : string ) => {
        var weight = extensionMap.get(oldExtension);
        setExtensionMap(prev => new Map([...prev, [newExtension, weight]]))
        handleRemoveExtension(oldExtension);
    };

    const handleWeightChange = (extension : string, weight : number) => {
        setExtensionMap(prev => new Map([...prev, [extension, weight]]))
    }


    const handleSave = () => {

        if(name==""){
            enqueueSnackbar('Profile must have a name', {variant: 'error',});
            return;
        }
        if(lineWeight != null && lineWeight < 0 || commentsWeight != null && commentsWeight < 0 || deleteWeight != null && deleteWeight < 0 || syntaxWeight != null && syntaxWeight < 0){
            enqueueSnackbar('Weights cannot be negative', {variant: 'error',});
            return;
        }
        if(lineWeight == null || commentsWeight == null || deleteWeight == null || syntaxWeight == null){
            enqueueSnackbar('Text fields must not be empty', {variant: 'error',});
            return;
        }
        for (let [key, value] of extensionMap) {
            if (value < 0){
                enqueueSnackbar('Extension weights cannot be negative', {variant: 'error',});
                return;
            }
        }

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
                }).catch(() => {
                    enqueueSnackbar('Failed to save score profile', {variant: 'error',});
                })
                close();
            } else {
                axios
                .post(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile` , newProfile, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    enqueueSnackbar('Successfully saved score profile', {variant: 'success',});
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
                                <AppTextField label="Name" value={name} onChange={(e) => setName( e.target.value)} required/>
                            </Box>
                        </div>
                        <Box display="flex" flexDirection="row" justifyContent="center" >
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="New Line" placeholder="Weight"
                                type="number"
                                value={lineWeight}
                                onChange={(e) => setLineWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Deleting" placeholder="Weight"
                                type="number"
                                value={deleteWeight}
                                onChange={(e) => setDeleteWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Syntax(e.g '}')" placeholder="Weight"
                                type="number"
                                value={syntaxWeight}
                                onChange={(e) => setSyntaxWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Comments" placeholder="Weight"
                                type="number"
                                value={commentsWeight}
                                onChange={(e) => setCommentsWeight(Number(e.target.value))}
                                />
                            </Box>
                        </Box>
                        <DialogTitle id="extension-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>{"Extensions"}</DialogTitle>
                        <Box  style={{ display:"flex", flexDirection:"column", justifyContent:"center", alignItems:"center"}} >
                            {extensionMap && extensionMap.size > 0 ?
                            Array.from(extensionMap).map((extension, index) => {
                                return (

                                    <Box
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
                                            value={extension[0]}
                                            onChange={(e) => handleExtensionChange(extension[0], e.target.value)}
                                            />
                                        </Box>
                                        <Box marginLeft={1} marginRight={1}>
                                            <AppTextField label="weight"
                                            value={extension[1]}
                                            onChange={(e) => handleWeightChange(extension[0], Number(e.target.value)) }
                                            type="number"
                                            />
                                        </Box>
                                        <div>

                                            <IconButton edge={false} aria-label="deleteextension" onClick={()=>handleRemoveExtension(extension[0])}>
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
