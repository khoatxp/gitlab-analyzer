package com.eris.gitlabanalyzer.controller;

import java.security.Principal;
import java.util.List;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.ScoreProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scoreprofile")
public class ScoreProfileController {

    private final ScoreProfileService scoreProfileService;
    private final AuthService authService;

    @Autowired
    public ScoreProfileController(ScoreProfileService scoreProfileService, AuthService authService){
        this.scoreProfileService = scoreProfileService;
        this.authService = authService;
    }

    @GetMapping
    public List<ScoreProfile> getScoreProfiles(Principal principal){
        var user = authService.getLoggedInUser(principal);
        return scoreProfileService.getUserScoreProfiles(user);
    }

    @GetMapping("/{id}")
    public ScoreProfile getScoreProfile(Principal principal, @PathVariable(value = "id") Long id) {
        var user = authService.getLoggedInUser(principal);
        return scoreProfileService.getScoreProfile(user, id);
    }

    @PostMapping
    public ScoreProfile createScoreProfile(Principal principal, @RequestBody ScoreProfile scoreProfile){
        var user = authService.getLoggedInUser(principal);
        return scoreProfileService.createScoreProfile(user, scoreProfile);
    }

    @PutMapping(path = "/{id}")
    public ScoreProfile updateScoreProfile(Principal principal, @PathVariable(value = "id") Long id, @RequestBody ScoreProfile scoreProfile){
        var user = authService.getLoggedInUser(principal);
        return scoreProfileService.updateScoreProfile(user, id, scoreProfile);
    }

    @DeleteMapping(path = "/{id}")
    public Long deleteScoreProfile(Principal principal, @PathVariable Long id) {
        var user = authService.getLoggedInUser(principal);
        return scoreProfileService.deleteScoreProfile(user, id);
    }

}
