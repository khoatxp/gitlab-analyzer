import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import {DeleteIcon, AddBoxIcon, EditIcon, AddCircleIcon} from '@material-ui/icons';
import {makeStyles} from "@material-ui/core/styles";
import Box from '@material-ui/core/Box';
import {AuthContext } from "./AuthContext";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import FormControl from '@material-ui/core/FormControl';
import Typography from '@material-ui/core/Typography';
import DialogContent from "@material-ui/core/DialogContent";
import TextField from "@material-ui/core/TextField";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import Select from '@material-ui/core/Select';
import { View } from 'react-native';
import ScoreProfile from "../interfaces/ScoreProfile";


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
    textInput:{
        margin:10
    }
}));

function Popup(props){
    const classes = useStyles();
    const { open, handleClose, id, scoreProfile } = props;
    const [extensionList, setExtensionList] = useState([{extension: "", weight: ""}]);
    const [comments, setComments] = React.useState<Number>();
    const [line, setLine] = React.useState<Number>();
    const [Delete, setDelete] = React.useState<Number>();
    const [name, setName] = React.useState<String>("") ;
    const [syntax, setSyntax] = React.useState<Number>();



    const close = () => {
        handleClose();
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

    const handleSubmit = e => {
        //();

    };

    if(id != null && scoreProfile!= null){
        setName(scoreProfile.nameWeight);
        setDelete(scoreProfile.DeleteWeight);
        setComments(scoreProfile.commentsWeight);
        setSyntax(scoreProfile.syntaxWeight);
        setExtensionList(scoreProfile.extensionWeight);
        setLine(scoreProfile.lineWeight);
    }

    return (

        <React.Fragment>
            <Dialog open={open} onClose={close} fullWidth maxWidth="sm">
                <button onClick={close}>X</button>
                <Typography align="center"> Score Profile</Typography>
                <DialogContent>
                    <form className={classes.root} onSubmit={handleSubmit}>
                        <div display="flex">
                            <TextField
                                name="name"
                                id="name"
                                label="Name"
                                type="text"
                                className={classes.TextInput}
                                value={name}
                                onChange={setName}
                            />
                            <TextField
                                name="new-line"
                                id="new-line"
                                label="New Line"
                                placeholder="Weight"
                                type="number"
                                className={classes.TextInput}
                                value={line}
                                onChange={setLine}
                            />
                            <TextField
                                name="delete"
                                id="delete"
                                label="Deleting"
                                placeholder="Weight"
                                type="number"
                                className={classes.TextInput}
                                value = {Delete}
                                onChange={setDelete}
                            />
                            <TextField
                                name="syntax"
                                id="Syntax"
                                label="Syntax"
                                placeholder="Weight"
                                type="number"
                                className={classes.TextInput}
                                value={syntax}
                                onChange={setSyntax}
                            />
                            <TextField
                                name="comments"
                                id="comments"
                                label="Comments"
                                placeholder="Weight"
                                type="number"
                                className={classes.TextInput}
                                value={comments}
                                onChange={setComments}
                            />
                        </div>
                        <div align="start">
                            <IconButton edge="start" aria-label="addextension" onClick={handleAddExtension}>
                                <AddCircleIcon style={{ fontSize: "25px", color: "green" }} />
                            </IconButton>
                        </div>
                        <View style={{flex: 1, flexDirection:"row", flexWrap:'wrap', alignItems:"flex-start"}}>
                            {extensionList.map((x, i) => {
                                return (

                                    <Box
                                        marginLeft="5px"
                                        marginRight="15px"
                                        //marginTop="3px"
                                        color="white"
                                        boxShadow={0}
                                        width="15vw"
                                        minWidth="15px"
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
                        </View>
                        <div align="end">
                            <button type="submit" variant="contained" color="primary">
                                Save
                            </button>
                        </div>
                    </form>
                </DialogContent>
            </Dialog>
        </React.Fragment>
    )
}

const ScoreProfileSelector = () => {

    const classes = useStyles();
    const[profiles, setProfiles] =  useState<ScoreProfile[]>([]);
    const profiles = ['a','b','c','d'];
    const [isIconVisible, setIconVisible] = React.useState(false);
    const[selectedProfile, setSelectedProfile] = useState<number>()
    const [open, setOpen] = React.useState(false);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
      
 
    /** 
    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile/profiles`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProfile(resp.data);
                    setIsLoading(false);
                });
            ;
        }
    });
    */

    const onProfileSelect = (_event: any, value: ScoreProfile) => {
        setSelectedProfile(value.id);
    }

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };


    return (
        <div className={classes.rowAlign}>
            <FormControl className={classes.formControl}>
                <InputLabel id="score-options">Score Options</InputLabel>
                <Select
                    labelId="score-options"
                    onOpen={() => setIconVisible(true)}
                    onClose={() => setIconVisible(false)}
                >
                    {profiles.map(p => (
                        <MenuItem value={p}>
                            {p}
                            {isIconVisible ? (
                                <ListItemSecondaryAction variant="outlined">
                                    <IconButton edge="end" aria-label="edit" onClick={handleOpen}>
                                        <EditIcon style={{ fontSize: "25px", color: "grey" }} />
                                    </IconButton>
                                    <Popup  open={open} handleClose={handleClose} />
                                    <IconButton edge="end" aria-label="delete">
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
            <Popup  open={open} handleClose={handleClose} />
        </div>
    );
}

export default ScoreProfileSelector;