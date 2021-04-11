import {useSnackbar} from "notistack";
import React, {useEffect, useState} from "react";
import {AuthContext} from "../components/AuthContext";
import axios, {AxiosResponse} from "axios";

interface Props {
  id: string | undefined
}

const MemberText =  ({id}: Props) => {
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [gitManagementUser, setGitManagementUser] = useState<string>();
    useEffect(()=>{
        if(id == undefined){
            enqueueSnackbar('Undefined Member ID encountered', {variant: 'error',});
            setGitManagementUser(`Member ??`);
        }
        else if(id == '0'){
            setGitManagementUser('Everyone');
        }
        else{
            axios.get(`${process.env.NEXT_PUBLIC_API_URL}/managementusers/members/${id}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setGitManagementUser(resp.data.name)
                })
                .catch(() => {
                    enqueueSnackbar('Failed to Retrieve Member Name', {variant: 'error',});
                    setGitManagementUser(`Member ${id}`);
                })
        }
    },[id])


    return gitManagementUser;
}

export default MemberText;