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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CalculateDiffMetrics {

    private final Map<String, String[]> commentCharacters = new HashMap<>();

    private final GitLabService gitLabService;
    private final FileScoreRepository fileScoreRepository;
    private final MergeRequestRepository mergeRequestRepository;
    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;

    private final int ModifiedLineBoilerPlateLength = 1;

    public enum lineTypes {
        code,
        comment,
        blockComment,
        syntax,
        removed,
    }

    public CalculateDiffMetrics(GitLabService gitLabService, FileScoreRepository fileScoreRepository,
                                MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository,
                                CommitRepository commitRepository){
        initializeCommentCharacters();
        this.gitLabService = gitLabService;
        this.fileScoreRepository = fileScoreRepository;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.commitRepository = commitRepository;
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

    public void storeMetricsCommit(long projectId, long commitId){

        if(fileScoreRepository.findByCommitId(commitId).isEmpty()){
            Commit commit = commitRepository.findById(commitId).orElse(null);
            Project project = projectRepository.findById(projectId).orElse(null);
            if(commit != null && project != null ){
                Iterable<GitLabFileChange> commitDiff = gitLabService.getCommitDiff(project.getGitLabProjectId(), commit.getSha()).toIterable();

                if(commitDiff.iterator().hasNext()){
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
    }

    public void storeMetricsMerge(long projectId, long mergeId){
        if(fileScoreRepository.findByMergeId(mergeId).isEmpty()){

            MergeRequest mergeRequest = mergeRequestRepository.findById(mergeId).orElse(null);
            Project project = projectRepository.findById(projectId).orElse(null);

            if(mergeRequest != null && project != null){
                Iterable<GitLabFileChange> merge = gitLabService.getMergeRequestDiff(project.getGitLabProjectId(), mergeRequest.getIid()).toIterable();

                if(merge.iterator().hasNext()){
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
        boolean inCommentBlock = false;
        String[] lines = diff.split("\n");
        String[] commentOperator = commentCharacters.getOrDefault(fileType, new String[]{" ", " "});
        String commentTerminator = commentOperator[commentOperator.length - 1];

        for(String line : lines){
            // remove whitespace
            line = line.replaceAll("\\s+","");
            if(inCommentBlock){
                lineTotals.put(lineTypes.comment, lineTotals.get(lineTypes.comment) + 1);
                if(line.contains(commentTerminator)){
                    inCommentBlock = false;
                }
            }else if(line.length() > 1 && !line.equals("\\Nonewlineatendoffile")){
                if(line.charAt(0) == '+'){
                    switch (typeOfLine(line, commentOperator)){
                        case code:
                            lineTotals.put(lineTypes.code, lineTotals.getOrDefault(lineTypes.code, 0) + 1);
                            break;
                        case comment:
                            lineTotals.put(lineTypes.comment, lineTotals.getOrDefault(lineTypes.comment, 0) + 1);
                            break;
                        case blockComment:
                            lineTotals.put(lineTypes.comment, lineTotals.getOrDefault(lineTypes.comment, 0) + 1);
                            inCommentBlock = true;
                            break;
                        case syntax:
                            lineTotals.put(lineTypes.syntax, lineTotals.getOrDefault(lineTypes.syntax, 0) + 1);
                            break;
                    }
                } else if(line.charAt(0) == '-'){
                    //TODO give proper weight to removing line
                    lineTotals.put(lineTypes.removed, lineTotals.getOrDefault(lineTypes.removed, 0) + 1);
                }
            }
        }
        return lineTotals;
    }

    private lineTypes typeOfLine(String line, String[] commentOperator){
        for(int i = 0; i < commentOperator.length; i++){
            //Todo find better way to handle single character syntax
            if(ModifiedLineBoilerPlateLength + commentOperator[i].length() > line.length()){
                return lineTypes.syntax;
            }
            String lineStartChar = line.substring(ModifiedLineBoilerPlateLength, (ModifiedLineBoilerPlateLength + commentOperator[i].length()));
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
