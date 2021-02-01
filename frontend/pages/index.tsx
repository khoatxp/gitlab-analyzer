import { Typography } from "@material-ui/core";
import React from "react";
import BackendTest from "../components/BackendTest";
import NavBar from "../components/NavBar";

export default function Home() {
  return (
    <>
      <NavBar />
      <Typography variant="h2">Welcome to GitLab Analyzer</Typography>
      <BackendTest />
    </>
  );
}
