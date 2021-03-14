import React, {useEffect} from "react";
import {useRouter} from "next/router";
import {AuthContext} from "../components/AuthContext";
import AuthView from "../components/AuthView";

export default function Home() {
    const {user} = React.useContext(AuthContext);
    // navigate to servers page because that is the start of the user flow
    const router = useRouter();
    useEffect(() => {
        if(user !== null) {
            router.push(`/server`)
        }
    });

    // Don't need to display anything, page is meant only to redirect
    return <AuthView>{}</AuthView>
}
