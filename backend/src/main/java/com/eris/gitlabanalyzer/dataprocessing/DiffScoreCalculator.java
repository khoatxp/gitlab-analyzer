package com.eris.gitlabanalyzer.dataprocessing;

import com.eris.gitlabanalyzer.model.FileScore;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.repository.FileScoreRepository;
import com.eris.gitlabanalyzer.repository.ScoreProfileRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DiffScoreCalculator {

    private ScoreProfile defaultScoreProfile;

    private FileScoreRepository fileScoreRepository;
    private ScoreProfileRepository scoreProfileRepository;

    public DiffScoreCalculator(FileScoreRepository fileScoreRepository, ScoreProfileRepository scoreProfileRepository){
        this.fileScoreRepository = fileScoreRepository;
        this.scoreProfileRepository = scoreProfileRepository;
        defaultScoreProfile = new ScoreProfile("defaultScoreProfile", 2, 0.5, 1, 1 );
        Map<String, Double> extensions = new HashMap<>();
        defaultScoreProfile.addExtension(extensions);
    }

    public double calculateScoreMerge(long mergeId, Long scoreProfileId){
        ScoreProfile scoreProfile = scoreProfileRepository.findScoreProfileById(scoreProfileId).orElse(defaultScoreProfile);
        List<FileScore> fileScores = fileScoreRepository.findByMergeId(mergeId);
        return calculateFileScore(fileScores, scoreProfile);
    }

    public double calculateScoreCommit(long commitId, Long scoreProfileId){
        ScoreProfile scoreProfile = scoreProfileRepository.findScoreProfileById(scoreProfileId).orElse(defaultScoreProfile);
        List<FileScore> fileScores = fileScoreRepository.findByCommitId(commitId);
        return calculateFileScore(fileScores, scoreProfile);
    }

    private double calculateFileScore(List<FileScore> fileScores, ScoreProfile scoreProfile){
        double totalScore = 0;
        for(FileScore fileScore : fileScores){
            double fileWeightModifier = scoreProfile.getExtensionWeights().getOrDefault(fileScore.getFileType(), 1.0);

            totalScore += fileScore.getCodeLineAdded() * (fileWeightModifier * scoreProfile.getLineWeight());
            totalScore += fileScore.getSyntaxLineAdded() * scoreProfile.getSyntaxWeight();
            totalScore += fileScore.getCommentLineAdded() *  scoreProfile.getCommentsWeight();
            totalScore += fileScore.getLineRemoved() * scoreProfile.getDeleteWeight();
        }

        return  totalScore;
    }



}
