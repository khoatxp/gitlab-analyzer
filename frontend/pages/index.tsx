import { Typography } from "@material-ui/core";
import React, {useEffect} from "react";
import NavBar from "../components/NavBar";
import AuthView from "../components/AuthView";
import {useRouter} from "next/router";

export default function Home() {
    // navigate to servers page because that is the start of the user flow
    const router = useRouter();
    useEffect(() => {
        router.push(`/server`)
    });

    return null;
}
