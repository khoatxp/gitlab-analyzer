import React, {useEffect} from "react";
import CodeAnalysis from "../../../../../components/CodeAnalysis";
import AuthView from "../../../../../components/AuthView";
import MenuLayout from "../../../../../components/layout/menu/MenuLayout";
import {useSnackbar} from "notistack";
import {useRouter} from "next/router";
import axios, {AxiosError, AxiosResponse} from "axios";
import {AuthContext} from "../../../../../components/AuthContext";

const index = () => {
    const {enqueueSnackbar} = useSnackbar();
    const router = useRouter();
    const { projectId } =  router.query;
    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/${projectId}/commits/authors?state=unmapped`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    if(resp.data.length > 0){
                        enqueueSnackbar(`Some members could not be mapped automatically. Please go to Settings > Members to make changes`,{variant: 'warning',})
                    }
                }).catch((err: AxiosError)=>{
                enqueueSnackbar(`Failed to get unmapped commit authors: ${err.message}`, {variant: 'error',});
            })
        }
    }, [projectId]);

    return (
        <AuthView>
            <MenuLayout tabSelected={0}>
                <CodeAnalysis/>
            </MenuLayout>
        </AuthView>
    );
};

export default index;
