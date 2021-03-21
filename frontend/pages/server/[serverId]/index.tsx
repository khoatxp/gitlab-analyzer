import AuthView from "../../../components/AuthView";
import CardLayout from "../../../components/layout/CardLayout";
import React from "react";
import {useRouter} from "next/router";
import {Box} from "@material-ui/core";
import AppButton from "../../../components/app/AppButton";

const index = () => {
    const router = useRouter();
    const {serverId} = router.query;

    return (
        <AuthView>
            <CardLayout backLink={"/server"} logoType="header">
                <Box display="flex" flexDirection="column">
                    <AppButton color="primary" onClick={() => router.push(`/server/${serverId}/projects`)}>Create a new analysis</AppButton>
                    <AppButton color="primary">Analyses from others</AppButton>
                    <AppButton color="primary" onClick={() => router.push(`/server/${serverId}/analyses`)}>My previous Analyses</AppButton>
                </Box>
            </CardLayout>
        </AuthView>
    )
}

export default index;