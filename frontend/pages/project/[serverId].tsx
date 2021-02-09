import { LinearProgress, TextField, Typography } from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../components/CardLayout";

interface GitLabProjectType {
  id: number,
  name: string,
  name_with_namespace: string,
  web_url: string,
}

const index = () => {
  const [projects, setProjects] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();
  const { serverId } =  router.query;

  useEffect(() => {
    if (router.isReady) {
      // TODO need to pass serverId into this call to get the correct gitlab url and access code from db
      // when that information is available in db
      axios
          .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects`)
          .then((resp: AxiosResponse) => {
            setProjects(resp.data);
            setIsLoading(false);
          });
    }
  }, [serverId]);

  let loadingBar =  null;
  if (isLoading) {
    loadingBar = <div>
      <Typography variant={"body1"}>
        Loading projects...
      </Typography>
      <LinearProgress />
    </div>;
  }
  else {
    loadingBar =  null;
  }
  return (
    <CardLayout>
      {loadingBar}
      {!isLoading && <Autocomplete
          id="project-select"
          options={projects as GitLabProjectType[]}
          getOptionLabel={(proj) => proj.name_with_namespace}
          renderInput={(params) => <TextField {...params} label="Search Projects" variant="outlined" />}
      /> }
    </CardLayout>
  );
};

export default index;
