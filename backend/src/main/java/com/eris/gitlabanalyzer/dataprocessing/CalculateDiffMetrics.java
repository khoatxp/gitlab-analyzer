package com.eris.gitlabanalyzer.dataprocessing;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.FileScore;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
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

    private enum lineTypes {
        code,
        comment,
        blockComment,
        syntax,
        removed,
    }

    public CalculateDiffMetrics(GitLabService gitLabService, FileScoreRepository fileScoreRepository,
                                MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository){
        initializeCommentCharacters();
        this.gitLabService = gitLabService;
        this.fileScoreRepository = fileScoreRepository;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
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
    //TODO finish after 34 is merged
    public void storeMetricsCommit(long projectId, long commitId){
        //TODO get commit sha from repo
        //Commit commit = commitRepository
        if(fileScoreRepository.findByCommitId(commitId) == null){
            //TODO pass sha retrieved from repo
            Iterable<GitLabFileChange> commitDiff = gitLabService.getCommitDiff(projectId, "sha").toIterable();
            //Todo get commit after 34 is merged
            //Commit commit =

            if(commitDiff != null){
                for(GitLabFileChange file : commitDiff){
                    Map<lineTypes, Integer> fileCount = countLineTypes(file.getDiff(), findFileType(file));
                }
            }

        }
    }

    public void storeMetricsMerge(long mergeId, long projectId){
        if(fileScoreRepository.findByMergeId(mergeId).isEmpty()){

            MergeRequest mergeRequest = mergeRequestRepository.findById(mergeId).orElse(null);
            Project project = projectRepository.findById(projectId).orElse(null);
            if(mergeRequest != null && project != null){
                Iterable<GitLabFileChange> merge = gitLabService.getMergeRequestDiff(projectId, mergeRequest.getIid()).toIterable();
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
