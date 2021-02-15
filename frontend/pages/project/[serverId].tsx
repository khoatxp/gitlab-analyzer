import { Box, Button, TextField, Typography, LinearProgress } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import React, {useState, useEffect} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../components/CardLayout";
import DatePicker from "../../components/DatePicker";
import {GitLabProject} from "../../interfaces/GitLabProject";
import Autocomplete from "@material-ui/lab/Autocomplete";

const useStyles = makeStyles((theme) => ({
    margin: {
        margin: theme.spacing(1),
    },
}));

const LoadingBar = () => {
  return <div>
    <Typography variant={"body1"}>
      Loading projects...
    </Typography>
    <LinearProgress />
  </div>;
}


const index = () => {

  const classes = useStyles();
  const [projects, setProjects] = useState<GitLabProject[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
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
    loadingBar = <LoadingBar />;
  }


  return (
      <CardLayout>
        {loadingBar}
        {!isLoading && <Autocomplete
            id="project-select"
            options={projects}
            getOptionLabel={(proj) => proj.name_with_namespace}
            renderInput={(params) => <TextField {...params} label="Search Projects" variant="outlined" />}
        /> }

        <div className='menu' style={{ marginTop: `30px` }}>

          <Box
              margin
              color="white"
              boxShadow={0}
              width="56vw"
              height="10vh"
              minWidth="260px"
              minHeight="10vh"
              display="flex"
              flexDirection="column"
              justifyContent="column"
              alignItems="column"
          >
            <div className='Date picker'>
              <DatePicker/>
            </ div>
          </Box>
        </div>

          <div align="center">
            <Button variant="contained" color="primary" size="medium" className={classes.margin} >
              ANALYZE
            </Button>
          </div>

      </CardLayout>

  );
};
export default index;
