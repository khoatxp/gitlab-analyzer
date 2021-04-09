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

    public List<ScoreProfile> getUserScoreProfiles(User user){
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

        ScoreProfile oldProfile = getScoreProfile(user, id);
        scoreProfile.setId(oldProfile.getId());
        scoreProfile.setUser(user);
        return this.scoreProfileRepository.save(scoreProfile);

    }


    public Long deleteScoreProfile(User user, Long id) {
        
        ScoreProfile scoreProfile = getScoreProfile(user, id);
        scoreProfileRepository.delete(scoreProfile);
        return id;
    }


}
