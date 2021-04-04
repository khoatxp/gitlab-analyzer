import {useSnackbar} from "notistack";
import React, {useState} from "react";
import {AuthContext} from "../components/AuthContext";
import axios, {AxiosResponse} from "axios";

const getMember =  (id: string | undefined) => {
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [gitManagementUser, setGitManagementUser] = useState<string>();

    if (id != undefined) {
        axios.get(`${process.env.NEXT_PUBLIC_API_URL}/managementusers/members/${id}`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setGitManagementUser(id !== '0' ? resp.data.name : 'Everyone');})
            .catch(() => {
                enqueueSnackbar('Failed to Retrieve Member Name', {variant: 'error',});
                setGitManagementUser(`Member ${id}`);
            })
    } else if (id == undefined) {
        enqueueSnackbar('Undefined Member ID encountered', {variant: 'error',});
        setGitManagementUser(`Member ??`);
    }

    return gitManagementUser;
}

export default getMember;