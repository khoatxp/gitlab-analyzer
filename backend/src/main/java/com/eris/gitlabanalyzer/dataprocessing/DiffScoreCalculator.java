package com.eris.gitlabanalyzer.dataprocessing;

import com.eris.gitlabanalyzer.model.GitLabFileChange;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//TODO create public method to read-in/update point values
@Service
public class DiffScoreCalculator {

    private Map<String, Integer> filePointValues = new HashMap<>();
    private final Map<String, String[]> commentCharacters = new HashMap<>();
    private int commentPointValue = 1;
    private final int DEFAULT_FILE_POINTS = 2;
    private enum lineTypes {
        code,
        comment,
        blockComment,
        syntax,
    }



    public DiffScoreCalculator(){
        initializeCommentCharacters();
    }

    private void initializeCommentCharacters(){
        commentCharacters.put("java", new String[]{"//", "/*", "*/"});
        commentCharacters.put("ts", new String[]{"//", "/*", "*/"});
        commentCharacters.put("js", new String[]{"//", "/*", "*/"});
        commentCharacters.put("tsx", new String[]{"//", "/*", "*/"});
        commentCharacters.put("c", new String[]{"//", "/*", "*/"});
        commentCharacters.put("cpp", new String[]{"//", "/*", "*/"});
        commentCharacters.put("py", new String[]{"#", "\"\"\""});

    }


    public int calculateScore(Iterable<GitLabFileChange> files){
        int totalScore = 0;

        for(GitLabFileChange file : files){
           totalScore += calculateFileScore(file.getDiff(), findFileType(file));
        }

        return totalScore;
    }

    public int calculateScore(GitLabFileChange file){
        return calculateFileScore(file.getDiff(), findFileType(file));
    }

    // Used to test scoreing directly without GitlabFileChange obj
    public int calculateScore(String diff, String fileType){
        return calculateFileScore(diff, fileType);
    }


    private String findFileType(GitLabFileChange file){
        String[] fileNameParsed;
        if(file.getNewPath() != null){
            fileNameParsed = file.getNewPath().split("\\.");
        } else {
            fileNameParsed = file.getOldPath().split("\\.");
        }
        return fileNameParsed[fileNameParsed.length -1];
    }

    //TODO look into handling of default values may not need function
    private int getFilePointValue(String fileType){
        return  filePointValues.getOrDefault(fileType, DEFAULT_FILE_POINTS);
    }

    private int calculateFileScore(String diff, String fileType){
        int totalScore = 0;
        int pointValue = getFilePointValue(fileType);
        boolean inCommentBlock = false;
        String[] lines = diff.split("\n");
        String[] commentOperator = commentCharacters.getOrDefault(fileType, new String[]{" ", " "});
        String commentTerminator = commentOperator[commentOperator.length - 1];

        for(String line : lines){
            // remove whitespace
            line = line.replaceAll("\\s+","");
            if(inCommentBlock){
                totalScore += commentPointValue;
                if(line.contains(commentTerminator)){
                    inCommentBlock = false;
                }
            }else if(line.length() > 1 && !line.equals("\\Nonewlineatendoffile")){
                if(line.charAt(0) == '+'){
                    switch (typeOfLine(line, commentOperator)){
                        case code:
                            totalScore += pointValue;
                            break;
                        case comment:
                            totalScore += commentPointValue;
                            break;
                        case blockComment:
                            totalScore += commentPointValue;
                            inCommentBlock = true;
                            break;
                    }
                } else{
                    //TODO give proper weight to removing line
                    totalScore += commentPointValue/2;
                }
            }
        }
        return totalScore;
    }

    private lineTypes typeOfLine(String line, String[] commentOperator){
        for(int i = 0; i < commentOperator.length; i++){
            //Todo find better way to handle single character syntax
            if(1+ commentOperator[i].length() > line.length()){
                return lineTypes.syntax;
            }
            String lineStartChar = line.substring(1, (1 + commentOperator[i].length()));
            if(lineStartChar.equals(commentOperator[i])){
                if(i > 0){
                    return lineTypes.blockComment;
                }
                return lineTypes.comment;
            }
        }
        return lineTypes.code;
    }

}
