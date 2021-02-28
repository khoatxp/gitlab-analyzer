package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.exception.RessourceNotFoundException;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
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
        ScoreProfile scoreProfile = scoreProfileRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Profile not found for this id : " + id));
        return ResponseEntity.ok().body(scoreProfile);
    }


    public ScoreProfile createScoreProfile(ScoreProfile scoreProfile){
        return this.scoreProfileRepository.save(scoreProfile);
    }

    public ResponseEntity<ScoreProfile> UpdateScoreProfile( Long id, ScoreProfile scoreProfile) throws RessourceNotFoundException{

        ScoreProfile oldProfile =  scoreProfileRepository.findById(id).orElseThrow(()->new RessourceNotFoundException("Profile not found for this id : " + id));
        oldProfile.setName(scoreProfile.getName());
        oldProfile.setComments(scoreProfile.getComments());
        oldProfile.setDelete(scoreProfile.getDelete());
        oldProfile.setLine(scoreProfile.getLine());
        oldProfile.setSyntax(scoreProfile.getSyntax());
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
