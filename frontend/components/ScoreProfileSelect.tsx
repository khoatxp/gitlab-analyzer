import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import ScoreProfile from "../interfaces/ScoreProfile";
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Box from '@material-ui/core/Box';
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import FormControl from '@material-ui/core/FormControl';
import EditIcon from '@material-ui/icons/Edit';
import AddBoxIcon from '@material-ui/icons/AddBox';
import DeleteIcon from "@material-ui/icons/Delete";
import IconButton from "@material-ui/core/IconButton";
import Select from '@material-ui/core/Select';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import {AuthContext} from "./AuthContext";
import {useSnackbar} from 'notistack';
import ScoreProfileModal from "./ScoreProfileModal";


const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 200,
        marginBottom: "15px"
    },
}));

interface Props {
    profile: ScoreProfile 
    setProfile: (profile:ScoreProfile) => void
}

const ScoreProfileSelector = ({profile, setProfile}:Props) => {


    const [isNewProfile, setIsNewProfile] = useState(true);
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
        setIsNewProfile(true)
        setSelectedProfile(null)
        setOpen(true);
    };

    const handleEdit = (Profile) => {
        setId(Profile.id)
        setIsNewProfile(false)
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
                    isLoading= "...loading profiles"
                    maxMenuHeight = {220}
                >
                    {profiles.map(profile => (
                        <MenuItem value={profile.id} key={profile.id}>
                            {profile.name}
                            {isIconVisible ? (
                                <ListItemSecondaryAction variant="outlined">
                                    <IconButton edge="end" aria-label="edit" onClick={() => { handleEdit(profile);}} >
                                        <EditIcon style={{ fontSize: "25px", color: "grey" }} />
                                    </IconButton>
                                    <ScoreProfileModal  open={open} handleClose={handleClose} id={id} profile={selectedProfile} isNewProfile={isNewProfile}/>
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
            <ScoreProfileModal  open={open} handleClose={handleClose} id={id} profile={selectedProfile} isNewProfile={isNewProfile}/>
  
        </Box>
    );
}

export default ScoreProfileSelector;