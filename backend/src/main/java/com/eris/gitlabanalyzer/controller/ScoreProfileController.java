package com.eris.gitlabanalyzer.controller;

import java.util.List;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/scoreprofile")
public class ScoreProfileController {

    @Autowired
    private ScoreProfileRepository scoreProfileRepository;


    @GetMapping(path = "/profiles")
    public List<ScoreProfile> getScoreProfiles(){
        return scoreProfileRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ScoreProfile getScoreProfile(@PathVariable Long id){
        return scoreProfileRepository.findById(id).orElse(null);
    }

    @CrossOrigin
    @PostMapping("/profile")
    public ScoreProfile createScoreProfile(@RequestBody ScoreProfile scoreProfile){
        return this.scoreProfileRepository.save(scoreProfile);
    }

    @CrossOrigin
    @PutMapping("/profile/{id}")
    public ScoreProfile UpdateScoreProfile(@PathVariable("id") Long id, @RequestBody ScoreProfile scoreProfile) {

        ScoreProfile oldProfile =  scoreProfileRepository.findById(id).orElse(null);
        oldProfile.setName(scoreProfile.getName());
        oldProfile.setComments(scoreProfile.getComments());
        oldProfile.setDelete(scoreProfile.getDelete());
        oldProfile.setLine(scoreProfile.getLine());
        oldProfile.setSyntax(scoreProfile.getSyntax());
        oldProfile.getExtension().forEach((k,v)-> scoreProfile.getExtension().put(k,v));
        return scoreProfileRepository.save(oldProfile);
    }

    @CrossOrigin
    @DeleteMapping("/profile/{id}")
    public Long DeleteScoreProfile(@PathVariable Long id){
        scoreProfileRepository.deleteById(id);
        return id;
    }

}
