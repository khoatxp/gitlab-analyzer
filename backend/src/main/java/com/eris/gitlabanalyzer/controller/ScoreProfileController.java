package com.eris.gitlabanalyzer.controller;

import java.util.List;

import com.eris.gitlabanalyzer.model.ScoreProfile;
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


    @GetMapping
    public List<ScoreProfile> getScoreProfiles(){
        return scoreProfileService.getScoreProfiles();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ScoreProfile> getScoreProfile(@PathVariable(value = "id") Long id) {
        return scoreProfileService.getScoreProfile(id);
    }

    @CrossOrigin
    @PostMapping
    public ScoreProfile createScoreProfile(@RequestBody ScoreProfile scoreProfile){
        return scoreProfileService.createScoreProfile(scoreProfile);
    }

    @CrossOrigin
    @PutMapping(path = "/{id}")
    public ResponseEntity<ScoreProfile> UpdateScoreProfile(@PathVariable(value = "id") Long id, @RequestBody ScoreProfile scoreProfile){
        return scoreProfileService.UpdateScoreProfile(id,scoreProfile);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> DeleteScoreProfile(@PathVariable Long id) {
        return scoreProfileService.DeleteScoreProfile(id);
    }

}
