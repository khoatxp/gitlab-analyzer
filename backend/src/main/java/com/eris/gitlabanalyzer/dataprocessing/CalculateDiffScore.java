package com.eris.gitlabanalyzer.dataprocessing;

import com.eris.gitlabanalyzer.model.GitLabFileChange;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CalculateDiffScore {

    private Map<String, Integer> filePointValues = new HashMap<>();
    private final Map<String, String[]> commentCharacters = new HashMap<>();
    private int commentPointValue = 1;


    //TODO read in point values
    public CalculateDiffScore(){
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
        Map<Integer, String[]> fileDictionary = new HashMap<>();
        int totalScore = 0;
        int fileCount = getIteratorSize(files.iterator());
        String[] fileTypes = new String[fileCount];

        int i = 0;
        // separate out lines and find file types for each file
        for(GitLabFileChange file : files){
           fileTypes[i] = findFileType(file);
           String[] lines = file.getDiff().split("\n");
           fileDictionary.put(i, lines);
           i++;
        }

        for(int j = 0; j < fileTypes.length; j++){
          totalScore += calculateFileScore(fileDictionary.get(j), fileTypes[j]);
        }
        return totalScore;

    }

    public int calculateScore(GitLabFileChange file){
        String fileType = findFileType(file);
        String[] lines = file.getDiff().split("\n");
        return calculateFileScore(lines, fileType);
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
        return  filePointValues.getOrDefault(fileType, 1);
    }

    private int calculateFileScore(String[] lines, String fileType){
        int scoreTotal = 0;
        int pointValue = getFilePointValue(fileType);
        boolean inCommentBlock = false;
        String[] commentOperator = commentCharacters.getOrDefault(fileType, new String[]{" ", " "});
        String commentTerminator = commentOperator[commentOperator.length - 1];
        for(String line : lines){
            // remove whitespace
            line = line.replaceAll("\\s+","");
            if(inCommentBlock){
                scoreTotal += commentPointValue;
                if(line.contains(commentTerminator)){
                    inCommentBlock = false;
                }
            }else if(line.length() > 1 && !line.equals("\\Nonewlineatendoffile")){
                if(line.charAt(0) == '+'){
                    String lineType = typeOfLine(line, commentOperator);
                    switch (lineType) {
                        case "code":
                            scoreTotal += pointValue;
                            break;
                        case "comment":
                            scoreTotal += commentPointValue;
                            break;
                        case "blockComment":
                            scoreTotal += commentPointValue;
                            inCommentBlock = true;
                            break;
                    }
                } else {
                    //TODO give proper weight to removing line
                    scoreTotal += commentPointValue/2;
                }

            }

        }
        return scoreTotal;
    }

    private String typeOfLine(String line, String[] commentOperator){
        for(int i = 0; i < commentOperator.length; i++){
            //Todo find better way to handle single character syntax
            if(1+ commentOperator[i].length() > line.length()){
                return "syntax";
            }
            String lineStartChar = line.substring(1, (1 + commentOperator[i].length()));
            if(lineStartChar.equals(commentOperator[i])){
                if(i > 0){
                    return "blockComment";
                }
                return "comment";
            }
        }
        return "code";
    }

    private int getIteratorSize(Iterator iterator) {
        int count = 0;
        while(iterator.hasNext()){
            count++;
            iterator.next();
        }
        return count;
    }


}
