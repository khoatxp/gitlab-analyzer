package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ScoreProfileService {

    private final ScoreProfileRepository scoreProfileRepository;

    public ScoreProfileService(ScoreProfileRepository scoreProfileRepository) {
        this.scoreProfileRepository = scoreProfileRepository;
    }

    public List<ScoreProfile> getScoreProfiles(){
        return scoreProfileRepository.findAll();
    }


    public ScoreProfile getScoreProfile(Long id) {
        return scoreProfileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for this id : " + id));
    }


    public ScoreProfile createScoreProfile(ScoreProfile scoreProfile){
        return this.scoreProfileRepository.save(scoreProfile);
    }

    public ScoreProfile updateScoreProfile( Long id, ScoreProfile scoreProfile) {

        ScoreProfile oldProfile =  scoreProfileRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for this id : " + id));
        scoreProfile.setId(oldProfile.getId());
        return this.scoreProfileRepository.save(scoreProfile);
    }


    public Long deleteScoreProfile( Long id) {
        ScoreProfile scoreProfile = scoreProfileRepository.findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for this id : " + id));
        scoreProfileRepository.delete(scoreProfile);
        return id;
    }


}
