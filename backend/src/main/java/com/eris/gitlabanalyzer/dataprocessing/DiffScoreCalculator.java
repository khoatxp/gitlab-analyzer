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


    public int calculateScoreMerge(long mergeId){
        List<FileScore> fileScores = fileScoreRepository.findByMergeId(mergeId);
        return calculateFileScore(fileScores);
    }

    public int calculateScoreCommit(long commitId){
        List<FileScore> fileScores = fileScoreRepository.findByCommitId(commitId);
        return calculateFileScore(fileScores);
    }

    //TODO look into handling of default values may not need function
    private int getFilePointValue(String fileType){
        return  filePointValues.getOrDefault(fileType, DEFAULT_FILE_POINTS);
    }

    //Todo update scoring values
    private int calculateFileScore(List<FileScore> fileScores){
        int totalScore = 0;
        for(FileScore fileScore : fileScores){
            int fileTypeWeight = filePointValues.getOrDefault(fileScore.getFileType(), DEFAULT_FILE_POINTS);
            totalScore += fileScore.getCodeLineAdded() * fileTypeWeight;
            totalScore += fileScore.getSyntaxLineAdded() * commentPointValue;
            totalScore += fileScore.getCommentLineAdded() * commentPointValue;
            totalScore += fileScore.getLineRemoved() * (fileTypeWeight/2);
        }

        return  totalScore;
    }



}
