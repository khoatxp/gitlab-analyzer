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
                    FileScore fileScore = new FileScore(commit, fileType, file.getNewPath(),
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

                    FileScore fileScore = new FileScore(mergeRequest, fileType, file.getNewPath(),
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

        // if no commentCharacter info exists defaults to nothing
        CommentCharacter commentOperator = commentCharacters.getOrDefault(fileType, new CommentCharacter("","",""));

        for(int lineNumber = 0; lineNumber < lines.length; lineNumber++){
            String line = lines[lineNumber].replaceAll("\\s+","");
            if(line.length() > 1 && !line.equals("\\Nonewlineatendoffile")) {

                if (line.charAt(0) == '+') {
                    line = line.substring(1);
                    switch (typeOfLine(line, commentOperator)) {
                        case code:
                            lineTotals.put(lineTypes.code, lineTotals.getOrDefault(lineTypes.code, 0) + 1);
                            //todo check if comment after code
                            break;
                        case comment:
                            lineTotals.put(lineTypes.comment, lineTotals.getOrDefault(lineTypes.comment, 0) + 1);
                            break;
                        case blockComment:
                            // todo check if just comment char on line
                            lineTotals.put(lineTypes.comment, lineTotals.getOrDefault(lineTypes.comment, 0) + 1);
                            while (!line.contains(commentOperator.getBlockCommentEnd())) {
                                lineNumber++;
                                line = lines[lineNumber].replaceAll("\\s+","");
                                lineTotals.put(lineTypes.comment, lineTotals.get(lineTypes.comment) + 1);
                            }
                            //todo check if just comment char on line
                            //todo check if code after comment
                            break;
                        case syntax:
                            lineTotals.put(lineTypes.syntax, lineTotals.getOrDefault(lineTypes.syntax, 0) + 1);
                            break;
                    }

                } else if (line.charAt(0) == '-') {
                    //TODO give proper weight to removing line
                    lineTotals.put(lineTypes.removed, lineTotals.getOrDefault(lineTypes.removed, 0) + 1);
                }
            }
        }
        return lineTotals;
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
}
