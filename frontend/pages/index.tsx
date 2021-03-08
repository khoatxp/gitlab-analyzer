import { Typography } from "@material-ui/core";
import React from "react";
import NavBar from "../components/NavBar";
import AuthView from "../components/AuthView";

export default function Home() {
  return (
    <AuthView>
      <NavBar />
      <Typography variant="h2">Welcome to GitLab Analyzer</Typography>
    </AuthView>
  );
}
