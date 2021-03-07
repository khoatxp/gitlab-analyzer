package com.eris.gitlabanalyzer.dataprocessing;

import com.eris.gitlabanalyzer.model.FileScore;
import com.eris.gitlabanalyzer.repository.FileScoreRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO create public method to read-in/update point values
@Component
public class DiffScoreCalculator {

    private Map<String, Integer> filePointValues = new HashMap<>();
    private int commentPointValue = 1;
    private final int DEFAULT_FILE_POINTS = 2;


    FileScoreRepository fileScoreRepository;

    public DiffScoreCalculator(FileScoreRepository fileScoreRepository){
        this.fileScoreRepository = fileScoreRepository;
    }


    public int calculateScore(long mergeId, long projectId){
        int totalScore = 0;

        List<FileScore> fileScores = fileScoreRepository.findByProjectIdAndMergeId(projectId, mergeId);
        for(FileScore fileScore : fileScores){
           totalScore += calculateFileScore(fileScore);
        }

        return totalScore;
    }

    //TODO look into handling of default values may not need function
    private int getFilePointValue(String fileType){
        return  filePointValues.getOrDefault(fileType, DEFAULT_FILE_POINTS);
    }

    //Todo update scoring values
    private int calculateFileScore(FileScore fileScore){
        int totalScore = 0;
        int fileTypeWeight = filePointValues.getOrDefault(fileScore.getFileType(), DEFAULT_FILE_POINTS);


        totalScore += fileScore.getCodeLineAdded() * fileTypeWeight;
        totalScore += fileScore.getSyntaxLineAdded() * commentPointValue;
        totalScore += fileScore.getCommentLineAdded() * commentPointValue;
        totalScore += fileScore.getLineRemoved() * (fileTypeWeight/2);

        return  totalScore;
    }



}
