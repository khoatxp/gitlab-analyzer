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
import AppTextField from "../../components/app/AppTextField";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from '@material-ui/core/DialogTitle';
import Select from '@material-ui/core/Select';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import {AuthContext} from "./AuthContext";


const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(5),
        minWidth: 150,
    },
    selectEmpty: {
        marginTop: theme.spacing(2),
    },
    rowAlign:{
        flexDirection: "row",
        justifyContent: "center",
        alignItems:"center",
    },
    blue:{
        backgroundColor: "8FC6F3",
        color:"8FC6F3"
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
        const { extension, value } = e.target;
        const list = [...extensionList];
        list[index][extension] = value;
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
            <Dialog open={open} onClose={close} fullWidth maxWidth="sm" style={{color:"red", backgroundColor: 'transparent',
                boxShadow: 'none'}} >
                <button onClick={close}>X</button>
                <DialogTitle id="edit-dialog-title" align="center">{"Score Profile"}</DialogTitle>
                <DialogContent>
                    <form className={classes.root} onSubmit={handleSave}>
                        <Box display="flex" justifyContent="flex-end">
                            <AppTextField placeholder="name" value={name} onChange={(e) => setName(e.target.value)}/>
                        </Box>
                        <Box display="flex" flexDirection="row" justifyContent="center" flexWrap="wrap">
                            <AppTextField placeholder="New Line" value={lineWeight} onChange={(e) => setLineWeight(e.target.value)}/>
                            <AppTextField placeholder="Deleting" value={deleteWeight} onChange={(e) => setDeleteWeight(e.target.value)}/>
                            <AppTextField placeholder="Syntax" value={syntaxWeight} onChange={(e) => setSyntaxWeight(e.target.value)}/>
                            <AppTextField placeholder="Comments" value={commentsWeight} onChange={(e) => setCommentsWeight(e.target.value)}/>
                        </Box>
                        <DialogTitle id="extension-dialog-title" align="center">{"Extensions"}</DialogTitle>
                        <Box  display="flex" flexDirection="row" justifyContent="center" flexWrap="wrap">
                            {extensionList.map((x, i) => {
                                return (

                                    <Box
                                        boxShadow={0}
                                        display="flex"
                                        flexDirection="column"
                                        justifyContent="column"
                                        alignItems="center"
                                    >
                                        <AppTextField placeholder="extension" value={x.extension} onChange={(e) => handleExtensionChange(e.target.value, i)}/>
                                        <AppTextField placeholder="weight" value={x.weight} onChange={(e) => handleExtensionChange(e.target.value, i)}/>
                                        <div className="btn-box">
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
                        <Button type="submit" variant="contained" color="primary" size="small" onClick={handleSave}>
                            Save
                        </Button>
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
        <Box display="flex" alignItems="row">
            <FormControl className={classes.formControl}>
                <InputLabel id="score-options">Score Options</InputLabel>
                <Select
                    labelId="score-options"
                    onOpen={() => setIconVisible(true)}
                    onClose={() => setIconVisible(false)}
                    value={profile}
                    onChange={setProfile}
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

            <IconButton edge="start" aria-label="add" onClick={handleOpen}>
                <AddBoxIcon style={{ fontSize: "25px", color: "green" }}/>
            </IconButton>
            <Popup  open={open} handleClose={handleClose} scoreProfile={selectedProfile}/>
        </Box>
    );
}

export default ScoreProfileSelector;