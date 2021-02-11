import React, {useState, useEffect} from "react";
import axios, {AxiosError, AxiosResponse} from "axios";
import { useSnackbar } from 'notistack';

const BackendTest = () => {
    const { enqueueSnackbar } = useSnackbar();
    const [repos, setRepos] = useState([]);

    // Load repo data
    useEffect(() => {
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/projects`)
            .then((resp: AxiosResponse) => {
                setRepos(resp.data);
                enqueueSnackbar(`Successfully connected to backend`, {variant: 'success',});
            })
            .catch((err: AxiosError) => {
                enqueueSnackbar(`Failed to connect to backend: ${err.message}`, {variant: 'error',});
            });
    }, []);

    return (
        <div>
            <h4>API URL: {process.env.NEXT_PUBLIC_API_URL}</h4>
            <p>API data: {JSON.stringify(repos)}</p>
        </div>
    );
};

export default BackendTest;
