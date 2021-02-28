package com.eris.gitlabanalyzer.controller;

import java.util.List;

import com.eris.gitlabanalyzer.exception.RessourceNotFoundException;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import com.eris.gitlabanalyzer.service.GitLabService;
import com.eris.gitlabanalyzer.service.ScoreProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/scoreprofile")
public class ScoreProfileController {

    private final ScoreProfileService scoreProfileService;

    @Autowired
    public ScoreProfileController(ScoreProfileService scoreProfileService){
        this.scoreProfileService = scoreProfileService;
    }


    @GetMapping(path = "/profiles")
    public List<ScoreProfile> getScoreProfiles(){
        return scoreProfileService.getScoreProfiles();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ScoreProfile> getScoreProfile(@PathVariable(value = "id") Long id) throws RessourceNotFoundException{
        return scoreProfileService.getScoreProfile(id);
    }

    @CrossOrigin
    @PostMapping("/profile")
    public ScoreProfile createScoreProfile(@RequestBody ScoreProfile scoreProfile){
        return scoreProfileService.createScoreProfile(scoreProfile);
    }

    @CrossOrigin
    @PutMapping("/profile/{id}")
    public ResponseEntity<ScoreProfile> UpdateScoreProfile(@PathVariable(value = "id") Long id, @RequestBody ScoreProfile scoreProfile) throws RessourceNotFoundException{
        return scoreProfileService.UpdateScoreProfile(id,scoreProfile);
    }

    @CrossOrigin
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Long> DeleteScoreProfile(@PathVariable Long id) throws RessourceNotFoundException {
        return scoreProfileService.DeleteScoreProfile(id);
    }

}
