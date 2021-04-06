package com.eris.gitlabanalyzer.service;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ScoreProfileService {

    private final ScoreProfileRepository scoreProfileRepository;

    public ScoreProfileService(ScoreProfileRepository scoreProfileRepository) {
        this.scoreProfileRepository = scoreProfileRepository;
    }

    public List<ScoreProfile> getScoreProfiles(User user){
        return scoreProfileRepository.findScoreProfilesByUserId(user.getId());
    }


    public ScoreProfile getScoreProfile(User user, Long id) {
        var userScoreProfiles = this.getScoreProfiles(user);
        Optional<ScoreProfile> scoreProfile = userScoreProfiles.stream().filter(s -> s.getId() == id).findFirst();
        if (scoreProfile.isPresent()){
            return scoreProfile.get();
        }
        else{
            throw new NoSuchElementException("Score Profile not found");
        }
    }


    public ScoreProfile createScoreProfile(User user, ScoreProfile scoreProfile){
        scoreProfile.setUser(user);
        return scoreProfileRepository.save(scoreProfile);
    }

    public ScoreProfile updateScoreProfile(User user, Long id, ScoreProfile scoreProfile) {

        var userScoreProfiles = this.getScoreProfiles(user);
        Optional<ScoreProfile> oldProfile = userScoreProfiles.stream().filter(s -> s.getId() == id).findFirst();
        if(oldProfile.isPresent()) {
            scoreProfile.setId(oldProfile.get().getId());
            scoreProfile.setUser(user);
            return this.scoreProfileRepository.save(scoreProfile);
        }
        else{
            throw new NoSuchElementException("Score Profile not found");
        }

    }


    public Long deleteScoreProfile(User user, Long id) {
        var userScoreProfiles = this.getScoreProfiles(user);
        Optional<ScoreProfile> scoreProfile = userScoreProfiles.stream().filter(s -> s.getId() == id).findFirst();
        if(scoreProfile.isPresent()) {
            scoreProfileRepository.delete(scoreProfile.get());
            return id;
        }
        else{
            throw new NoSuchElementException("Score Profile not found") ;
        }
    }


}
