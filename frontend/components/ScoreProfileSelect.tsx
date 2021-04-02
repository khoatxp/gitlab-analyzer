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
import {AuthContext} from "./AuthContext";
import {useSnackbar} from 'notistack';
import ScoreProfileModal from "./ScoreProfileModal";


const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 200,
        marginBottom: "15px"
    },
    menuPaper: {
        maxHeight: 200,
    },
}));

interface Props {
    onScoreProfileSelect: (id: number) => void
}

const ScoreProfileSelector = ({onScoreProfileSelect}:Props) => {



    const classes = useStyles();
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    const [scoreProfile, setScoreProfile] = useState<number>();
    const [profiles, setProfiles] =  useState<ScoreProfile[]>([]);
    const [isIconVisible, setIconVisible] = useState(false);
    const [open, setOpen] = useState(false);
    const [selectOpen, setSelectOpen] = useState(false);
    const [isNewProfile, setIsNewProfile] = useState(true);
    const [selectedProfile, setSelectedProfile] = useState<ScoreProfile | null>(null);
    const [id,setId] = useState<number>(0)

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
    },[]);

    const handleDelete = (id : number) =>{
        if (router.isReady) {
            axios.delete(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile/${id}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    console.log(resp.data);
                    if(scoreProfile == id){
                        setScoreProfile(undefined);
                    }
                    update();
                }).catch(() => {
                enqueueSnackbar('Failed to delete score profile', {variant: 'error',});
            })
        }

    };

    const update = () => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/scoreprofile`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setSelectOpen(false);
                    setProfiles(resp.data);
                }).catch(() => {
                enqueueSnackbar('Failed to retrieve score profiles', {variant: 'error',});
            })
        }
    }

    const handleNew = () => {
        setId(0);
        setIsNewProfile(true)
        setSelectedProfile(null)
        setOpen(true);
    };

    const handleEdit = (Profile : ScoreProfile) => {
        setId(Profile.id)
        setIsNewProfile(false)
        setSelectedProfile(Profile)
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <Box display="flex" flexDirection="row" justifyContent="center" marginLeft={4}>
            <FormControl className={classes.formControl}>
                    <InputLabel id="score-options">Score Options</InputLabel>
                    <Select
                        labelId={"score-options"}
                        open={selectOpen}
                        onOpen={() => {setIconVisible(true); setSelectOpen(true)}}
                        onClose={() => {setIconVisible(false); setSelectOpen(false)}}
                        value={scoreProfile || ""}
                        onChange={(e) => {setScoreProfile(Number(e.target.value)); onScoreProfileSelect(Number(e.target.value))}}
                        MenuProps={{
                            anchorOrigin: {vertical: "top", horizontal: "left"},
                            transformOrigin: {vertical: "top", horizontal: "left"},
                            getContentAnchorEl: null,
                            classes: {paper: classes.menuPaper }}}
                    >
                        {profiles.length > 0 ? (
                            profiles.map((profile, index :number) => (
                                <MenuItem value={profile.id} key={index}>
                                    {profile.name}
                                    {isIconVisible ? (
                                        <ListItemSecondaryAction >
                                            <IconButton edge="end" aria-label="edit" onClick={() => { handleEdit(profile);}} >
                                                <EditIcon style={{ fontSize: "25px", color: "grey" }} />
                                            </IconButton>
                                            <ScoreProfileModal  open={open} handleClose={handleClose} id={id} profile={selectedProfile} isNewProfile={isNewProfile} update={update}/>
                                            <IconButton edge="end" aria-label="delete"  onClick={() => { handleDelete(profile.id);}}>
                                                <DeleteIcon style={{ fontSize: "25px", color: "#CC160B" }}/>
                                            </IconButton>
                                        </ListItemSecondaryAction>
                                    ) : null}
                                </MenuItem>
                        ))): <div> No profiles have been created </div> }
                    </Select>
            </FormControl>

            <IconButton edge={false} aria-label="add" onClick={handleNew} >
                <AddBoxIcon style={{ fontSize: "25px", color: "green" }}/>
            </IconButton>
            <ScoreProfileModal  open={open} handleClose={handleClose} id={id} profile={selectedProfile} isNewProfile={isNewProfile} update={update}/>


        </Box>
    );
}

export default ScoreProfileSelector;