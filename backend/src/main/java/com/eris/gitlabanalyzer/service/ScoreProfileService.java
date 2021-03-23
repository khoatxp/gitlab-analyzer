package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.UserServer;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreProfileService {

    private final ScoreProfileRepository scoreProfileRepository;

    public ScoreProfileService(ScoreProfileRepository scoreProfileRepository, UserServerRepository userServerRepository) {
        this.scoreProfileRepository = scoreProfileRepository;
    }

    public List<ScoreProfile> getScoreProfiles(User user){
        return scoreProfileRepository.findScoreProfilesByUserId(user.getId());
    }


    public ScoreProfile getScoreProfile(User user, Long id) {
        var userScoreProfiles = this.getScoreProfiles(user);
        Optional<ScoreProfile> scoreProfile = userScoreProfiles.stream().filter(s -> s.getId() == id).findFirst();
        return scoreProfile.get();
    }


    public ScoreProfile createScoreProfile(User user, ScoreProfile scoreProfile){
        scoreProfile.setUser(user);
        return scoreProfileRepository.save(scoreProfile);
    }

    public ScoreProfile updateScoreProfile(User user, Long id, ScoreProfile scoreProfile) {

        var userScoreProfiles = this.getScoreProfiles(user);
        Optional<ScoreProfile> oldProfile = userScoreProfiles.stream().filter(s -> s.getId() == id).findFirst();
        scoreProfile.setId(oldProfile.get().getId());
        scoreProfile.setUser(user);
        return this.scoreProfileRepository.save(scoreProfile);
    }


    public Long deleteScoreProfile(User user, Long id) {
        var userScoreProfiles = this.getScoreProfiles(user);
        Optional<ScoreProfile> scoreProfile = userScoreProfiles.stream().filter(s -> s.getId() == id).findFirst();
        scoreProfileRepository.delete(scoreProfile.get());
        return id;
    }


}
