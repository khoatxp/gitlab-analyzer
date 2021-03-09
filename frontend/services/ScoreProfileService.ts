import axios from "axios"

const SCORE_PROFILE_REST_API_URL = "http://localhost:8080/api/v1/scoreprofile/";

class ScoreProfileService{

    getAll(){
        return axios.get(SCORE_PROFILE_REST_API_URL);
    }

    get(id){
        return axios.get(SCORE_PROFILE_REST_API_URL+"/${id}");
    }

    create(profile){
        return axios.post(SCORE_PROFILE_REST_API_URL+, profile);
    }

    delete(id){
        return axios.delete(SCORE_PROFILE_REST_API_URL+"profile/${id}");
    }

    update(id, profile){
        return axios.put(SCORE_PROFILE_REST_API_URL+"profile/${id}", profile)
    }
}
export default new ScoreProfileService()