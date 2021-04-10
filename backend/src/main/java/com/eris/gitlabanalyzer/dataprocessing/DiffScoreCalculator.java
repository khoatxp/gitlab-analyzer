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

    private final ScoreProfile defaultScoreProfile;

    private final FileScoreRepository fileScoreRepository;
    private final ScoreProfileRepository scoreProfileRepository;

    public DiffScoreCalculator(FileScoreRepository fileScoreRepository, ScoreProfileRepository scoreProfileRepository){
        this.fileScoreRepository = fileScoreRepository;
        this.scoreProfileRepository = scoreProfileRepository;
        defaultScoreProfile = new ScoreProfile("defaultScoreProfile", 2, 0.5, 1, 1 );
        Map<String, Double> extensions = new HashMap<>();
        defaultScoreProfile.addExtension(extensions);
    }

    public double calculateScoreMerge(Long mergeId, Long scoreProfileId){
        ScoreProfile scoreProfile = scoreProfileRepository.findScoreProfileById(scoreProfileId).orElse(defaultScoreProfile);
        List<FileScore> fileScores = fileScoreRepository.findByMergeId(mergeId);
        return calculateFileScore(fileScores, scoreProfile);
    }

    public double calculateScoreCommit(Long commitId, Long scoreProfileId){
        ScoreProfile scoreProfile = scoreProfileRepository.findScoreProfileById(scoreProfileId).orElse(defaultScoreProfile);
        List<FileScore> fileScores = fileScoreRepository.findByCommitId(commitId);
        return calculateFileScore(fileScores, scoreProfile);
    }

    private double calculateFileScore(List<FileScore> fileScores, ScoreProfile scoreProfile){
        double totalScore = 0;
        for(FileScore fileScore : fileScores){
            if(!scoreProfile.blackListContains(fileScore.getFileType())) {
                double fileWeightModifier = scoreProfile.getExtensionWeights().getOrDefault(fileScore.getFileType(), 1.0);
                double codeWeight = fileWeightModifier * scoreProfile.getLineWeight();
                totalScore += fileScore.getCodeLineAdded() * codeWeight;
                totalScore += fileScore.getSyntaxLineAdded() * scoreProfile.getSyntaxWeight();
                totalScore += fileScore.getCommentLineAdded() *  scoreProfile.getCommentsWeight();

                totalScore += fileScore.getCodeLineRemoved() * Math.min(codeWeight, scoreProfile.getDeleteWeight());
                totalScore += fileScore.getSyntaxLineRemoved() * Math.min(scoreProfile.getSyntaxWeight(), scoreProfile.getDeleteWeight());
                totalScore += fileScore.getCommentLineRemoved() * Math.min(scoreProfile.getCommentsWeight(), scoreProfile.getDeleteWeight());
            }

        }

        return  totalScore;
    }



}
