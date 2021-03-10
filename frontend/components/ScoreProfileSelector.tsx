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
import TextField from "@material-ui/core/TextField";
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
    const [commentsWeight, setCommentsWeight] = React.useState<Number>();
    const [id, setId] = React.useState<Number>();
    const [lineWeight, setLineWeight] = React.useState<Number>();
    const [deleteWeight, setDeleteWeight] = React.useState<Number>();
    const [name, setName] = React.useState<String>("") ;
    const [syntaxWeight, setSyntaxWeight] = React.useState<Number>();
    const [update, setUpdate] = React.useState(false);
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
                        <Box style={{flex: 1, flexDirection: 'row', justifyContent: 'flex-end'}}>
                            <TextField
                                name="name"
                                id="name"
                                label="Name"
                                type="text"
                                value={name}
                                onChange={setName}
                            />
                        </Box>
                        <Box style={{ flex: 1, flexDirection: 'row', justifyContent: 'space-around', flexWrap:'wrap'}}>
                            <TextField
                                name="new-line"
                                id="new-line"
                                label="New Line"
                                placeholder="Weight"
                                type="number"
                                value={lineWeight}
                                onChange={setLineWeight}
                            />
                            <TextField
                                name="delete"
                                id="delete"
                                label="Deleting"
                                placeholder="Weight"
                                type="number"
                                value = {deleteWeight}
                                onChange={setDeleteWeight}
                            />
                            <TextField
                                name="syntax"
                                id="Syntax"
                                label="Syntax"
                                placeholder="Weight"
                                type="number"
                                value={syntaxWeight}
                                onChange={setSyntaxWeight}
                            />
                            <TextField
                                name="comments"
                                id="comments"
                                label="Comments"
                                placeholder="Weight"
                                type="number"
                                value={commentsWeight}
                                onChange={setCommentsWeight}
                            />
                        </Box>
                        <DialogTitle id="extension-dialog-title" align="center">{"Extensions"}</DialogTitle>
                        <Box style={{flex: 1, flexDirection:"row", flexWrap:'wrap', alignItems:"flex-start", justifyContent: 'space-around'}}>
                            {extensionList.map((x, i) => {
                                return (

                                    <Box
                                        boxShadow={0}
                                        display="flex"
                                        flexDirection="column"
                                        justifyContent="column"
                                        alignItems="center"
                                    >
                                        <TextField
                                            name="extension"
                                            value={x.extension}
                                            label="extension"
                                            type="text"
                                            onChange={e => handleExtensionChange(e, i)}
                                        />
                                        <TextField
                                            value={x.weight}
                                            name="weight"
                                            label="weight"
                                            type="number"
                                            onChange={e => handleExtensionChange(e, i)}
                                        />
                                        <div className="btn-box">
                                            {extensionList.length !== 1 &&
                                            <IconButton edge="center" aria-label="deleteextension" onClick={handleRemoveExtension}>
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
    const [isIconVisible, setIconVisible] = React.useState(false);
    const[selectedProfile, setSelectedProfile] = useState<ScoreProfile | null>()
    const [open, setOpen] = React.useState(false);
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
    },[id];

    return (
        <div className={classes.rowAlign}>
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
                        <MenuItem value={p}>
                            {p}
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
        </div>
    );
}

export default ScoreProfileSelector;