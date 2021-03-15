import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import ScoreProfile from "../interfaces/ScoreProfile";
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import DialogActions from '@material-ui/core/DialogActions';
import FormControl from '@material-ui/core/FormControl';
import DialogContent from "@material-ui/core/DialogContent";
import EditIcon from '@material-ui/icons/Edit';
import AddBoxIcon from '@material-ui/icons/AddBox';
import DeleteIcon from "@material-ui/icons/Delete";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from '@material-ui/core/DialogTitle';
import Select from '@material-ui/core/Select';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import {AuthContext} from "./AuthContext";
import AppTextField from "./app/AppTextField";
import AppButton from "./app/AppButton";
import {useSnackbar} from 'notistack';


const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 200,
        marginBottom: "15px"
    },
    popup:{
        borderRadius:45,
        padding:"20px",
        boxShadow:'none'
    },
    button: {
        borderRadius: "20px",
        padding: "12px 30px",
        margin: "10px",
        color:"primary"
    }
}));

interface Props {
    profile: ScoreProfile 
    setProfile: (x:ScoreProfile) => void
}

const Popup = (props) => {
    const classes = useStyles();
    const router = useRouter();
    const { open, handleClose, id, profile  } = props;
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);


    const[savedArray, setSavedArray] = useState<{}>({});
    const[Profile, setProfile] = useState<ScoreProfile>();
    const [extensionMap, setExtensionMap] = useState(new Map())
    const [syntaxWeight, setSyntaxWeight] = useState<number>()
    const [commentsWeight, setCommentsWeight] = useState<number>();
    const [name, setName] = useState<string>()
    const [lineWeight, setLineWeight] = useState<number>();
    const [deleteWeight, setDeleteWeight] = useState<number>();
    
    useEffect(() => {

        if(id > 0){
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
                    setCommentsWeight();
                    setDeleteWeight();
                    setLineWeight();
                    setSyntaxWeight();
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
        setExtensionMap((prev) => new Map(prev.clear()));
        setSavedArray({});
        handleClose();
    };

    const handleAddExtension = () => {
        setExtensionMap(prev => new Map([...prev, ["", ""]]))
    };

    const handleRemoveExtension = (extension) => {
        setExtensionMap((prev) => {
            const newMap = new Map(prev);
            newMap.delete(extension);
            return newMap;
        });
    };

    const handleExtensionChange = (oldExtension, newExtension ) => {
        var weight = extensionMap.get(oldExtension);
        setExtensionMap(prev => new Map([...prev, [newExtension, weight]]))
        handleRemoveExtension(oldExtension);     
    };

    const handleWeightChange = (extension, weight) => {
        setExtensionMap(prev => new Map([...prev, [extension, weight]]))
    }


    const handleSave = () => {

        if(name==""){
            enqueueSnackbar('Profile must have a name', {variant: 'error',});
            return;
        }
        if(lineWeight < 0 || commentsWeight < 0 || deleteWeight < 0 || syntaxWeight < 0){
            enqueueSnackbar('Weights cannot be negative', {variant: 'error',});
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

            if (id != 0) {
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
                <Button variant="contained" color="primary" className={classes.button} onClick={close}>X</Button>
                <DialogTitle id="edit-dialog-title" align="center">{"Score Profile"}</DialogTitle>
                <DialogContent>
                    <form className={classes.root} onSubmit={handleSave}>
                        <div marginLeft={2} align="right">
                            <Box width={150}>
                                <AppTextField label="Name" value={name} onChange={(e) => setName( e.target.value)} required/>
                            </Box>
                        </div>
                        <Box display="flex" flexDirection="row" justifyContent="center" >
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="New Line" placeholder="Weight" 
                                type="number" 
                                value={lineWeight}
                                onChange={(e) => setLineWeight(e.target.value)}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Deleting" placeholder="Weight" 
                                type="number" 
                                value={deleteWeight}
                                onChange={(e) => setDeleteWeight(e.target.value)}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Syntax(e.g '}')" placeholder="Weight" 
                                type="number" 
                                value={syntaxWeight}
                                onChange={(e) => setSyntaxWeight(e.target.value)}
                                />
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Comments" placeholder="Weight" 
                                type="number" 
                                value={commentsWeight}
                                onChange={(e) => setCommentsWeight(e.target.value)}
                                />
                            </Box>
                        </Box>
                        <DialogTitle id="extension-dialog-title" align="center">{"Extensions"}</DialogTitle>
                        <Box  display="flex" flexDirection="row" justifyContent="center" flexWrap="wrap">
                            {extensionMap && extensionMap.size > 0 ?
                            Array.from(extensionMap).map((x, index) => {
                                return (

                                    <Box
                                        boxShadow={0}
                                        display="flex"
                                        marginRight={3}
                                        marginLeft={3}
                                        flexDirection="column"
                                        justifyContent="column"
                                        alignItems="center"
                                    >
                                        <AppTextField label="extension" 
                                        value={x[0]} 
                                        onChange={(e) => handleExtensionChange(x[0], e.target.value)} 
                                        />
                                        <AppTextField label="weight" 
                                        value={x[1]} 
                                        onChange={(e) => handleWeightChange(x[0], e.target.value) } 
                                        type="number"
                                        />

                                        <div>
                                            
                                            <IconButton edge="center" aria-label="deleteextension" onClick={()=>handleRemoveExtension(x[0])}>
                                                <DeleteIcon style={{ fontSize: "25px", color:"grey" }} />
                                            </IconButton>
                                        </div>
                                    </Box>
                                );
                            }): "No extensions set for this Profile"}
                        </Box>
                        <div align="start">
                            <IconButton edge="start" aria-label="addextension" onClick={handleAddExtension}>
                                <AddCircleIcon style={{ fontSize: "30px", color: "green" }} />
                            </IconButton>
                        </div>
                    </form>
                </DialogContent>
                <DialogActions>
                    <div align="end">
                    <AppButton size="small" type="submit" color="primary" onClick={handleSave}>Save</AppButton>
                    </div>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    )
}

const ScoreProfileSelector = ({profile, setProfile}:Props) => {

    const classes = useStyles();
    const [profiles, setProfiles] =  useState<ScoreProfile[]>([]);
    const [isIconVisible, setIconVisible] = useState(false);
    const [open, setOpen] = useState(false);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const [selectedProfile, setSelectedProfile] = useState<ScoreProfile | null>();
    const [id,setId] = useState<number>()


    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProfiles(resp.data);
                }).catch(() => {
                    enqueueSnackbar('Failed to retrieve score profiles', {variant: 'error',});
                })
        }
    });

    const handleNew = () => {
        setId(0);
        setSelectedProfile(null)
        setOpen(true);
    };

    const handleEdit = (Profile) => {
        setId(Profile.id)
        setSelectedProfile(Profile)
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleDelete = (id) =>{
        if (router.isReady) {
            axios.delete(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile/${id}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    console.log(resp.data);
                }).catch(() => {
                    enqueueSnackbar('Failed to delete score profile', {variant: 'error',});
                })
        }
    };

    return (
        <Box display="flex" flexDirection="row" justifyContent="row" marginLeft={4}>
        <FormControl className={classes.formControl}>
                <InputLabel id="score-options">Score Options</InputLabel>
                <Select
                    labelId="score-options"
                    onOpen={() => setIconVisible(true)}
                    onClose={() => setIconVisible(false)}
                    value={profile}
                    onChange={setProfile}
                    isLoading= "...loading"
                    maxMenuHeight = {220}
                    MenuProps={{
                        getContentAnchorEl: null,                     
                        anchorOrigin: {
                          vertical: "bottom",
                          horizontal: "left",
                        }
                        
                    }}
                >
                    {profiles.map(profile => (
                        <MenuItem value={profile.id} key={profile.id}>
                            {profile.name}
                            {isIconVisible ? (
                                <ListItemSecondaryAction variant="outlined">
                                    <IconButton edge="end" aria-label="edit" onClick={() => { handleEdit(profile);}} >
                                        <EditIcon style={{ fontSize: "25px", color: "grey" }} />
                                    </IconButton>
                                    <Popup  open={open} handleClose={handleClose} id={id} profile={selectedProfile}/>
                                    <IconButton edge="end" aria-label="delete"  onClick={() => { handleDelete(profile.id);}}>
                                        <DeleteIcon style={{ fontSize: "25px", color: "#CC160B" }}/>
                                    </IconButton>
                                </ListItemSecondaryAction>
                            ) : null}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl> 
            <IconButton aria-label="add" onClick={handleNew} marginTop={5}>
                    <AddBoxIcon style={{ fontSize: "25px", color: "green" }}/>
            </IconButton>
            <Popup  open={open} handleClose={handleClose} id={id} profile={selectedProfile}/>
  
        </Box>
    );
}

export default ScoreProfileSelector;