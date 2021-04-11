import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import ScoreProfile from "../interfaces/ScoreProfile";
import { makeStyles } from '@material-ui/core/styles';
import Typography from "@material-ui/core/Typography";
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


    const [savedArray, setSavedArray] = useState({});
    const [extensions, setExtensions] = useState<[string, number][]>([]);
    const [blackList, setBlackList] = useState<string[]>([]);
    const [syntaxWeight, setSyntaxWeight] = useState<number>()
    const [commentsWeight, setCommentsWeight] = useState<number>();
    const [name, setName] = useState<string>()
    const [lineWeight, setLineWeight] = useState<number>();
    const [deleteWeight, setDeleteWeight] = useState<number>();

    useEffect(() => {

        if(isNewProfile == false && profile != null){
            setName(profile.name);
            setCommentsWeight(profile.commentsWeight);
            setDeleteWeight(profile.deleteWeight);
            setLineWeight(profile.lineWeight);
            setSyntaxWeight(profile.syntaxWeight);
            setSavedArray({});
            setExtensions(Object.entries(profile.extensionWeights));
            setBlackList(profile.blackList);
            
        }
        else{
            setName("");
            setCommentsWeight(undefined);
            setDeleteWeight(undefined);
            setLineWeight(undefined);
            setSyntaxWeight(undefined);
            setExtensions([]);  
            setSavedArray({});
            setBlackList([]);
        }

    },[open])

    useEffect(() => {
        setSavedArray({});
        const list = Object.fromEntries(extensions);
        setSavedArray(list);
    }, [JSON.stringify(extensions)]);


    const close = () => {
        handleClose();
    };

    const handleAddExtension = () => {
        const list = extensions.slice();
        list.push(["", 0])
        setExtensions(list);
    };

    const handleRemoveExtension = (index : number) => {
        const list = extensions.slice();
        list.splice(index,1);
        setExtensions(list)
    };

    const handleExtensionChange = (extension : string , index : number ) => {
        const list = extensions.slice();
        list[index][0] = extension;
        setExtensions(list);
    };

    const handleWeightChange = (weight : number, index : number) => {
        const list = extensions.slice();
        list[index][1] = weight;
        setExtensions(list);
    }

    const addToBlackList = () => {
        const list = blackList.slice();
        list.push("")
        setBlackList(list);
    }

    const removeFromBlackList = (index: number) => {
        const list = blackList.slice();
        list.splice(index,1);
        setBlackList(list);
    }

    const handleBlacklistChange = (index: number, extension: string) => {
        const list = blackList.slice();
        list[index] = extension;
        setBlackList(list);
    }

    const validateBlackList= () => {
        const uniqueBlackList = new Set(blackList.map(extension => extension));
        if(uniqueBlackList.size < blackList.length){ //checks for duplicates
           enqueueSnackbar('Duplicate ignored extensions entered', {variant: 'error',});
           return false;
        }
        if (blackList.some( extension => extension === "" )){
            enqueueSnackbar('Ignored extension names must not be empty', {variant: 'error',});
            return false;
        }
        return true;
    }


    const validateExtensions = () =>  {

        const uniqueExtension = new Set(extensions.map(extension => extension[0]));
        if(uniqueExtension.size < extensions.length){ //checks for duplicates
           enqueueSnackbar('Duplicate extensions entered', {variant: 'error',});
           return false;
        }
        if (extensions.some( extension => extension[0] === "")){
            enqueueSnackbar('Extension names must not be empty', {variant: 'error',});
            return false;
        }
        if (extensions.some( extension => extension[1] < 0 || extension[1] == undefined)){
            enqueueSnackbar('Extension weights cannot be empty or negative', {variant: 'error',});
            return false;
        }
        else{
            return true;
        }
    }

    const handleSave = () => {

        if(name==""){
            enqueueSnackbar('Profile must have a name', {variant: 'error',});
            return;
        }
        if(lineWeight == undefined || commentsWeight == undefined || deleteWeight == undefined || syntaxWeight == undefined){
            enqueueSnackbar('Text fields must not be empty', {variant: 'error',});
            return;
        }
        if(lineWeight < 0 || commentsWeight < 0 || deleteWeight < 0 || syntaxWeight < 0){
            enqueueSnackbar('Weights cannot be negative', {variant: 'error',});
            return;
        }

       
        if (validateExtensions() && validateBlackList()){

            if (router.isReady) {

                const newProfile = {
                    name: name,
                    lineWeight: lineWeight,
                    deleteWeight: deleteWeight,
                    syntaxWeight: syntaxWeight,
                    commentsWeight: commentsWeight,
                    extensionWeights: savedArray,
                    blackList: blackList,
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
        }    

    };


    return (

        <React.Fragment>
            <Dialog open={open} onClose={close} fullWidth maxWidth="md" classes={{paper: classes.popup}} >
                <DialogTitle id="edit-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>{"Score Profile"}</DialogTitle>
                <DialogContent>
                    <form onSubmit={handleSave}>
                        <div style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>
                            <Box width={175}>
                                <AppTextField label="Name" value={name ?? ""} onChange={(e) => setName( e.target.value)} required/>
                            </Box>
                        </div>
                        <Box display="flex" flexDirection="row" justifyContent="center" >
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="New Line" placeholder="Weight"
                                type="number"
                                value={lineWeight != undefined ? lineWeight.toString() : ""}
                                onChange={(e) => setLineWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Deleting" placeholder="Weight"
                                type="number"
                                value={deleteWeight != undefined ? deleteWeight.toString() : ""}
                                onChange={(e) => setDeleteWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Syntax(e.g '}')" placeholder="Weight"
                                type="number"
                                value={syntaxWeight != undefined ? syntaxWeight.toString() : ""}
                                onChange={(e) => setSyntaxWeight(Number(e.target.value))}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Comments" placeholder="Weight"
                                type="number"
                                value={commentsWeight != undefined ? commentsWeight.toString() : ""}
                                onChange={(e) => setCommentsWeight(Number(e.target.value))}
                                />
                            </Box>
                        </Box>
                        <DialogTitle id="extension-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>
                            <Box style={{ display:"flex", justifyContent:"center"}}>
                                {"Extensions"}
                            </Box>
                            <Box style={{ display:"flex", justifyContent:"center"}}>
                                <Typography variant="caption">
                                    <b>Set weights for file extensions, unset file extensions will be given a default weight of 1 (do not include the dot)</b> 
                                </Typography>
                            </Box>
                        </DialogTitle>
                        <Box  style={{ display:"flex", flexDirection:"row", justifyContent:"center", alignItems:"center", flexWrap:"wrap"}} >
                            {extensions && extensions.length > 0 ?
                            extensions.map((extension, index: number) => {
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
                                        <Box marginLeft={1} marginRight={1} width={100}>
                                            
                                            <AppTextField label="extension"
                                            placeholder="Do not include the dot"
                                            value={extension[0] ?? ""}
                                            onChange={(e) => handleExtensionChange(e.target.value ,index)}
                                            />
                                        </Box>
                                        <Box marginLeft={1} marginRight={1} width={100}>
                                            
                                            <AppTextField label="weight"
                                            value={extension[1] != undefined ? extension[1].toString() : ""}
                                            onChange={(e) => handleWeightChange(Number(e.target.value), index) }
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
                        <DialogTitle id="blacklist-dialog-title" style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>
                            <Box style={{ display:"flex", justifyContent:"center"}}>
                            {"Ignored Extensions"}
                            </Box>
                            <Box style={{ display:"flex", justifyContent:"center"}}>
                                <Typography variant="caption">
                                    <b>Set file extensions to be ignored from score computation (do not include the dot)</b> 
                                </Typography>
                            </Box>
                        </DialogTitle>
                        <Box  style={{ display:"flex", flexDirection:"row", justifyContent:"center", alignItems:"center", flexWrap:"wrap"}} >
                            {blackList && blackList.length > 0 ?
                            blackList.map((extension, index: number) => {
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
                                        <Box marginLeft={1} marginRight={1} width={100}>

                                            <AppTextField label="extension"
                                            value={extension ?? ""}
                                            onChange={(e) => handleBlacklistChange(index, e.target.value)}
                                            />
                                        </Box>
                                        <div>

                                            <IconButton edge={false} aria-label="deleteextension" onClick={()=>removeFromBlackList(index)}>
                                                <DeleteIcon style={{ fontSize: "25px", color:"grey" }} />
                                            </IconButton>
                                        </div>
                                    </Box>
                                );
                            }): "No ignored extensions set for this profile"}
                        </Box>
                        <div style={{ display:"flex", justifyContent:"center", alignItems:"center"}}>
                            <IconButton edge={false} aria-label="addextension" onClick={addToBlackList}>
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
