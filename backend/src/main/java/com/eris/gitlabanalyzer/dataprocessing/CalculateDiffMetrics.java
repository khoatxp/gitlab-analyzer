package com.eris.gitlabanalyzer.dataprocessing;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.FileScore;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.FileScoreRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CalculateDiffMetrics {

    private final Map<String, CommentCharacter> commentCharacters = new HashMap<>();

    private final FileScoreRepository fileScoreRepository;
    private final MergeRequestRepository mergeRequestRepository;
    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;


    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public enum lineTypes {
        code,
        comment,
        blockComment,
        syntax,
        removed,
    }

    public enum lineAction {
        added,
        removed,
        unchanged,
    }

    public CalculateDiffMetrics(FileScoreRepository fileScoreRepository,
                                MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository,
                                CommitRepository commitRepository){
        initializeCommentCharacters();
        this.fileScoreRepository = fileScoreRepository;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.commitRepository = commitRepository;
    }

    private void initializeCommentCharacters(){
        commentCharacters.put("java", new CommentCharacter("//", "/*", "*/"));
        commentCharacters.put("ts", new CommentCharacter("//", "/*", "*/"));
        commentCharacters.put("js", new CommentCharacter("//", "/*", "*/"));
        commentCharacters.put("tsx", new CommentCharacter("//", "/*", "*/"));
        commentCharacters.put("c", new CommentCharacter("//", "/*", "*/"));
        commentCharacters.put("cpp", new CommentCharacter("//", "/*", "*/"));
        commentCharacters.put("py", new CommentCharacter("#", "\"\"\"", "\"\"\""));
    }

    public void storeMetricsCommit(long projectId, long commitId){
        var gitLabService = new GitLabService(serverUrl, accessToken);
        if(fileScoreRepository.findByCommitId(commitId).isEmpty()){

            Commit commit = commitRepository.findById(commitId).orElse(null);
            Project project = projectRepository.findById(projectId).orElse(null);
            if(commit != null && project != null ){
                Iterable<GitLabFileChange> commitDiff = gitLabService.getCommitDiff(project.getGitLabProjectId(), commit.getSha()).toIterable();

                // todo find a way to simpilfy this
                for(GitLabFileChange file : commitDiff){
                    String fileType = findFileType(file);
                    Map<lineTypes, Integer> fileCount = countLineTypes(file.getDiff(), fileType);
                    FileScore fileScore = new FileScore(commit, fileType.toLowerCase(), file.getNewPath(),
                            fileCount.getOrDefault(lineTypes.code, 0), fileCount.getOrDefault(lineTypes.syntax,0),
                            fileCount.getOrDefault(lineTypes.comment,0), fileCount.getOrDefault(lineTypes.removed,0));
                    fileScoreRepository.save(fileScore);
                }
            }
        }
    }

    public void storeMetricsMerge(long projectId, long mergeId){
        if(fileScoreRepository.findByMergeId(mergeId).isEmpty()){
            var gitLabService = new GitLabService(serverUrl, accessToken);
            MergeRequest mergeRequest = mergeRequestRepository.findById(mergeId).orElse(null);
            Project project = projectRepository.findById(projectId).orElse(null);

            if(mergeRequest != null && project != null){
                Iterable<GitLabFileChange> merge = gitLabService.getMergeRequestDiff(project.getGitLabProjectId(), mergeRequest.getIid()).toIterable();

                for(GitLabFileChange file : merge){
                    String fileType = findFileType(file);
                    Map<lineTypes, Integer> fileCount = countLineTypes(file.getDiff(), fileType);

                    FileScore fileScore = new FileScore(mergeRequest, fileType.toLowerCase(), file.getNewPath(),
                            fileCount.getOrDefault(lineTypes.code, 0), fileCount.getOrDefault(lineTypes.syntax,0),
                            fileCount.getOrDefault(lineTypes.comment,0), fileCount.getOrDefault(lineTypes.removed,0));

                    fileScoreRepository.save(fileScore);
                }
            }
        }
    }

    /**
     * Testing only function, used to access raw count.
     */
    public void testCalculateLines(String diff, String fileType, MergeRequest testMr ){

        Map<lineTypes, Integer> fileCount = countLineTypes(diff,fileType);
        FileScore fileScore = new FileScore(testMr, fileType, "TestPath",
                fileCount.getOrDefault(lineTypes.code, 0), fileCount.getOrDefault(lineTypes.syntax,0),
                fileCount.getOrDefault(lineTypes.comment,0), fileCount.getOrDefault(lineTypes.removed,0));

        fileScoreRepository.save(fileScore);
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

    private Map<lineTypes, Integer> countLineTypes(String diff, String fileType){
        Map<lineTypes, Integer> lineTotals = new HashMap<>();
        String[] lines = diff.split("\n");

        // if no commentCharacter info exists defaults to space which is trimmed at start of line parsing
        CommentCharacter commentOperators = commentCharacters.getOrDefault(fileType, new CommentCharacter(" "," "," "));

        for(int lineNumber = 0; lineNumber < lines.length; lineNumber++){

            String line = lines[lineNumber];
            lineAction action = getAction(line);
            line = trimForEval(line, action);

            if(line.length() > 0 && !line.equals("\\Nonewlineatendoffile")) {

                switch (typeOfLine(line, commentOperators)) {
                    case code:
                        updateTotal(action, lineTypes.code, lineTotals);
                        break;
                    case comment:
                        updateTotal(action, lineTypes.comment, lineTotals);
                        break;
                    case blockComment:
                        // handle case where line contains only comment operator
                        if(commentOperators.getBlockCommentStart().equals(line)) {
                            updateTotal(action, lineTypes.syntax, lineTotals);
                        } else{
                            updateTotal(action, lineTypes.comment, lineTotals);
                        }
                        while (!line.contains(commentOperators.getBlockCommentEnd())) {
                            lineNumber++;
                            line = lines[lineNumber];
                            action = getAction(line);
                            line = trimForEval(line, action);
                            updateTotal(action, lineTypes.comment, lineTotals);
                        }
                        // handles case where comment operator is by itself on line
                        if(line.startsWith(commentOperators.getBlockCommentEnd())&& action == lineAction.added) {
                            updateTotal(action, lineTypes.syntax, lineTotals);
                            lineTotals.put(lineTypes.comment, lineTotals.get(lineTypes.comment) - 1);
                        }
                        lineTypes secondType = isHybrid(line, commentOperators.getBlockCommentEnd());
                        if(secondType != lineTypes.comment){
                            updateTotal(action, secondType, lineTotals);
                        }
                        break;
                    case syntax:
                        updateTotal(action, lineTypes.syntax, lineTotals);
                        break;
                }
            }
        }
        return lineTotals;
    }

    private lineAction getAction(String line){
        if(line.length() >= 2){
            String startChar = line.substring(0,2);
            if(startChar.equals("+ ")){
                return lineAction.added;
            } else if(startChar.equals("- ")){
                return  lineAction.removed;
            } else{
                return lineAction.unchanged;
            }
        }
        return  lineAction.unchanged;
    }

    private String trimForEval(String line, lineAction action){
        line = line.replaceAll("\\s+","");
        if(action != lineAction.unchanged){
            line = line.substring(1);
        }
        return line;
    }

    private void updateTotal(lineAction action, lineTypes type,  Map<lineTypes, Integer> lineTotals){
        if(action == lineAction.added){
            lineTotals.put(type, lineTotals.getOrDefault(type, 0) + 1);
        } else if(action == lineAction.removed){
            lineTotals.put(lineTypes.removed, lineTotals.getOrDefault(lineTypes.removed, 0) + 1);
        }
    }

    private lineTypes typeOfLine(String line, CommentCharacter commentOperator){
        if(isComment(line, commentOperator.getSingleLineComment())){
            return lineTypes.comment;
        }
        if(isComment(line, commentOperator.getBlockCommentStart())){
            return lineTypes.blockComment;
        }
        if(isSyntax(line)){
            return lineTypes.syntax;
        }
        return lineTypes.code;
    }

    private boolean isComment(String line, String operator){
        // handle edge case to avoid index out of bound exception
        if(line.length() >= operator.length()){
            String lineStartChar = line.substring(0, operator.length());
            if(lineStartChar.equals(operator)){
                return true;
            }
        }
        return false;
    }

    //Makes assumption that every syntax doesn't contains alphanumeric chars
    private boolean isSyntax(String line){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return false;
        }
        return true;
    }

    private lineTypes isHybrid(String line, String operator){
        if(line.contains(operator) && !line.endsWith(operator)){
           int commentEndIndex = line.indexOf(operator);
           // add operator lengths so substring is exclusive of operator
           String secondSection = line.substring(commentEndIndex + operator.length());
           if(isSyntax(secondSection)){
               return lineTypes.syntax;
           }
           return lineTypes.code;
        }
        return lineTypes.comment;
    }

}
