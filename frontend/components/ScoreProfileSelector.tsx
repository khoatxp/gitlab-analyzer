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


const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 150,
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

function Popup(props){
    const classes = useStyles();
    const router = useRouter();
    const { open, handleClose, scoreProfile } = props;
    const [extensionList, setExtensionList] = useState([{extension: "", weight: ""}]);
    const [commentsWeight, setCommentsWeight] = useState<number>();
    const [id, setId] = useState<number>();
    const [lineWeight, setLineWeight] = useState<number>();
    const [deleteWeight, setDeleteWeight] = useState<number>();
    const [name, setName] = useState<string>("") ;
    const [syntaxWeight, setSyntaxWeight] = useState<number>();
    const [update, setUpdate] = useState(false);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);



    const close = () => {
        handleClose(update);
    };

    const handleAddExtension = () => {
        setExtensionList([...extensionList, { extension: "", weight: "" }]);
    };

    const handleRemoveExtension = index => {
        const list = [...extensionList];
        list.splice(index, 1);
        setExtensionList(list);
    };

    const handleExtensionChange = (e, index) => {
        const { extension, weight } = e.target.value;
        const list = [...extensionList];
        list[index][extension] = weight;
        setExtensionList(list);
    };

    const handleSave = () => {

        if (router.isReady) {

            const newProfile = {
                name: name,
                lineWeight: lineWeight,
                deleteWeight: deleteWeight,
                syntaxWeight: syntaxWeight,
                commentsWeight: commentsWeight,
                extensionWeights: extensionList
            }

            if (id == null) {
                axios
                    .put(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile`, newProfile, getAxiosAuthConfig())
                    .then((resp: AxiosResponse) => {
                        console.log(resp.data);
                    });
            } else {
                axios
                    .post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile/${id}` , newProfile, getAxiosAuthConfig())
                    .then((resp: AxiosResponse) => {
                        console.log(resp.data);
                    });
            }
            setUpdate(true)
        }

    };

    if( scoreProfile!= null){
        setName(scoreProfile.name);
        setDeleteWeight(scoreProfile.deleteWeight);
        setCommentsWeight(scoreProfile.commentsWeight);
        setSyntaxWeight(scoreProfile.syntaxWeight);
        setExtensionList(scoreProfile.extensionWeights);
        setLineWeight(scoreProfile.lineWeight);
        setId(scoreProfile.id);
    }

    return (

        <React.Fragment>
            <Dialog open={open} onClose={close} fullWidth maxWidth="sm" classes={{paper: classes.popup}} >
                <button className={classes.button} onClick={close}>X</button>
                <DialogTitle id="edit-dialog-title" align="center">{"Score Profile"}</DialogTitle>
                <DialogContent>
                    <form className={classes.root} onSubmit={handleSave}>
                        <div marginLeft={2} align="right">
                            <Box width={150}>
                                <AppTextField label="name" value={name} onChange={(e) => setName(e.target.value)}/>
                            </Box>
                        </div>
                        <Box display="flex" flexDirection="row" justifyContent="center" >
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="New Line" placeholder="Weight" value={lineWeight} onChange={(e) => setLineWeight(e.target.value)}/>
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Deleting" placeholder="Weight" value={deleteWeight} onChange={(e) => setDeleteWeight(e.target.value)}/>
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Syntax(e.g '}')" placeholder="Weight" value={syntaxWeight} onChange={(e) => setSyntaxWeight(e.target.value)}/>
                            </Box>
                            <Box marginLeft={1} marginRight={1}>
                                <AppTextField label="Comments" placeholder="Weight" value={commentsWeight} onChange={(e) => setCommentsWeight(e.target.value)}/>
                            </Box>
                        </Box>
                        <DialogTitle id="extension-dialog-title" align="center">{"Extensions"}</DialogTitle>
                        <Box  display="flex" flexDirection="row" justifyContent="center" flexWrap="wrap">
                            {extensionList.map((x, i) => {
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
                                        <AppTextField label="extension" value={x.extension} onChange={(e) => handleExtensionChange(e.target.value, i)}/>
                                        <AppTextField label="weight" value={x.weight} onChange={(e) => handleExtensionChange(e.target.value, i)}/>
                                        <div>
                                            {extensionList.length !== 1 &&
                                            <IconButton edge="center" aria-label="deleteextension" onClick={()=>handleRemoveExtension(i)}>
                                                <DeleteIcon style={{ fontSize: "25px", color: "grey" }} />
                                            </IconButton>}
                                        </div>
                                    </Box>
                                );
                            })}
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
    const[profiles, setProfiles] =  useState<ScoreProfile[]>([]);
    const [isIconVisible, setIconVisible] = useState(false);
    const[selectedProfile, setSelectedProfile] = useState<ScoreProfile | null>()
    const [open, setOpen] = useState(false);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
      

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProfiles(resp.data);
                });
            ;
        }
    });

    const update= () =>{
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProfiles(resp.data);
                });
        }
    }


    const onProfileSelect = (_event: any, value: ScoreProfile) => {
        setSelectedProfile(value);
    }

    const handleOpen = () => {
        setOpen(true);
    };

    const handleEdit = (selected) => {
        setOpen(true);
        setSelectedProfile(selected);
    };

    const handleClose = (update) => {
        setOpen(false);
        if(selectedProfile != null) {
            setSelectedProfile(null);
            if(update == true) {
                update();
            }
        }
    };

    const handleDelete = (id) =>{
        if (router.isReady) {
            axios.delete(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile/${id}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    console.log(resp.data);
                });
        }
    };

    return (
        <Box display="flex" flexDirection="row" justifyContent="row" marginLeft={5}>
        <FormControl className={classes.formControl}>
                <InputLabel id="score-options">Score Options</InputLabel>
                <Select
                    labelId="score-options"
                    onOpen={() => setIconVisible(true)}
                    onClose={() => setIconVisible(false)}
                    value={profile}
                    onChange={setProfile}
                    MenuProps={{
                        getContentAnchorEl: null,
                        anchorOrigin: {
                          vertical: "bottom",
                          horizontal: "left",
                        }
                    }}
                >
                    {profiles.map(p => (
                        <MenuItem value={p}>  //value{p.name}=
                            {p.name}
                            {isIconVisible ? (
                                <ListItemSecondaryAction variant="outlined">
                                    <IconButton edge="end" aria-label="edit" onClick={() => { handleEdit(p);}} >
                                        <EditIcon style={{ fontSize: "25px", color: "grey" }} />
                                    </IconButton>
                                    <Popup  open={open} handleClose={handleClose} scoreProfile={selectedProfile}/>
                                    <IconButton edge="end" aria-label="delete"  onClick={() => { handleDelete(p.id);}}>
                                        <DeleteIcon style={{ fontSize: "25px", color: "#CC160B" }}/>
                                    </IconButton>
                                </ListItemSecondaryAction>
                            ) : null}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl> 
            <IconButton aria-label="add" onClick={handleOpen} marginTop={5}>
                    <AddBoxIcon style={{ fontSize: "25px", color: "green" }}/>
            </IconButton>
            <Popup  open={open} handleClose={handleClose} scoreProfile={selectedProfile}/>             
  
        </Box>
    );
}

export default ScoreProfileSelector;