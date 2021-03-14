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

    private Map<String, Double> filePointWeighting;
    private final Map<String, Double> defaultWeighting =  new HashMap<>();
    private Map<lineTypes, Double> lineValues = new HashMap<>();

    enum lineTypes{
        code,
        comment,
        syntax,
        removal
    }

    private final double DEFAULT_FILE_POINTS = 2;


    private FileScoreRepository fileScoreRepository;
    private ScoreProfileRepository scoreProfileRepository;

    public DiffScoreCalculator(FileScoreRepository fileScoreRepository, ScoreProfileRepository scoreProfileRepository){
        this.fileScoreRepository = fileScoreRepository;
        this.scoreProfileRepository = scoreProfileRepository;
        loadDefaultProfile();
    }

    private void defaultValues(){
        lineValues.put(lineTypes.code, 2.0);
        lineValues.put(lineTypes.comment, 1.0);
        lineValues.put(lineTypes.syntax, 1.0);
        lineValues.put(lineTypes.removal, 0.5);
    }


    public double calculateScoreMerge(long mergeId){
        List<FileScore> fileScores = fileScoreRepository.findByMergeId(mergeId);
        return calculateFileScore(fileScores);
    }

    public double calculateScoreCommit(long commitId){
        List<FileScore> fileScores = fileScoreRepository.findByCommitId(commitId);
        return calculateFileScore(fileScores);
    }


    public void loadProfile(String scoreProfileName){
        ScoreProfile scoreProfile = scoreProfileRepository.findScoreProfileByName(scoreProfileName).orElse(null);
        if(scoreProfile != null){
            filePointWeighting = scoreProfile.getExtensionWeights();
            lineValues.put(lineTypes.code, scoreProfile.getLineWeight());
            lineValues.put(lineTypes.comment, scoreProfile.getCommentsWeight());
            lineValues.put(lineTypes.syntax, scoreProfile.getSyntaxWeight());
            lineValues.put(lineTypes.removal, scoreProfile.getDeleteWeight());
        } else {
            System.out.println("Error: Unable to load profile setting to default");
           loadDefaultProfile();
        }
    }

    private void loadDefaultProfile(){
        filePointWeighting = defaultWeighting;
        defaultValues();
    }


    //Todo update scoring values
    private double calculateFileScore(List<FileScore> fileScores){
        double totalScore = 0;
        for(FileScore fileScore : fileScores){
            double fileWeightModifier = filePointWeighting.getOrDefault(fileScore.getFileType(), 1.0);

            totalScore += fileScore.getCodeLineAdded() * (fileWeightModifier * lineValues.get(lineTypes.code));
            totalScore += fileScore.getSyntaxLineAdded() * (fileWeightModifier * lineValues.get(lineTypes.syntax));
            totalScore += fileScore.getCommentLineAdded() * (fileWeightModifier * lineValues.get(lineTypes.comment));
            totalScore += fileScore.getLineRemoved() * (fileWeightModifier * lineValues.get(lineTypes.removal));
        }

        return  totalScore;
    }



}
