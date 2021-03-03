package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.exception.RessourceNotFoundException;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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


    public ResponseEntity<ScoreProfile> getScoreProfile(Long id) throws RessourceNotFoundException {
        ScoreProfile scoreProfile = scoreProfileRepository.findById(id).orElseThrow(() -> throw new ResponseStatusException(NOT_FOUND, "Profile not found for this id : " + id));
        return ResponseEntity.ok().body(scoreProfile);
    }


    public ScoreProfile createScoreProfile(ScoreProfile scoreProfile){
        return this.scoreProfileRepository.save(scoreProfile);
    }

    public ResponseEntity<ScoreProfile> UpdateScoreProfile( Long id, ScoreProfile scoreProfile) throws RessourceNotFoundException{

        ScoreProfile oldProfile =  scoreProfileRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("Profile not found for this id : " + id));
        oldProfile.setName(scoreProfile.getName());
        oldProfile.setCommentsWeight(scoreProfile.getCommentsWeight());
        oldProfile.setDeleteWeight(scoreProfile.getDeleteWeight());
        oldProfile.setLineWeight(scoreProfile.getLineWeight());
        oldProfile.setSyntaxWeight(scoreProfile.getSyntaxWeight());
        oldProfile.getExtension().forEach((k,v)-> scoreProfile.getExtension().put(k,v));
        final ScoreProfile updatedProfile = scoreProfileRepository.save(oldProfile);
        return ResponseEntity.ok(updatedProfile);
    }


    public ResponseEntity<Long> DeleteScoreProfile( Long id) throws RessourceNotFoundException {
        ScoreProfile scoreProfile = scoreProfileRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Profile not found for this id : " + id));
        scoreProfileRepository.delete(scoreProfile);
        return ResponseEntity.ok(id);
    }


}
