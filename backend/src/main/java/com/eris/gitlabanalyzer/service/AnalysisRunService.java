package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabProject;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnalysisRunService {
    private AnalysisRunRepository analysisRunRepository;
    private final MessageService messageService;
    private final GitLabService requestScopeGitLabService;
    private final ProjectService projectService;

    @Autowired
    public AnalysisRunService(AnalysisRunRepository analysisRunRepository, GitLabService requestScopeGitLabService, MessageService messageService, ProjectService projectService) {
        this.analysisRunRepository = analysisRunRepository;
        this.requestScopeGitLabService = requestScopeGitLabService;
        this.messageService = messageService;
        this.projectService = projectService;
    }

    public AnalysisRun createAnalysisRun(
            User owner,
            Project project,
            AnalysisRun.Status status,
            OffsetDateTime startDateTime,
            OffsetDateTime endDateTime) {
        AnalysisRun analysisRun = new AnalysisRun(
                owner,
                project,
                project.getServer(),
                status,
//                scoreProfile, TODO: hookup once implemented
                startDateTime,
                endDateTime
        );
        return analysisRunRepository.save(analysisRun);
    }

    public Stream<AnalysisRunView> getAccessibleAnalysisRuns(User user, Long serverId) {
        List<Long> userAccessibleGitlabProjectIds = getUserAccessibleGitlabProjectIds();
        List<AnalysisRun> analysisRuns = analysisRunRepository.findOthersByServerIdAndGitLabProjectIds(user.getId(), serverId, userAccessibleGitlabProjectIds);
        createUserProjectPermissionForAnalysisRun(user, analysisRuns);
        return analysisRuns.stream().map(AnalysisRunView::fromAnalysisRun);
    }

    private List<Long> getUserAccessibleGitlabProjectIds() {
        List<GitLabProject> gitLabProjects = requestScopeGitLabService
                .getProjects()
                .collectList()
                .block();
        if (gitLabProjects != null) {
            return gitLabProjects
                    .stream()
                    .map(GitLabProject::getId)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void createUserProjectPermissionForAnalysisRun(User user, List<AnalysisRun> analysisRuns) {
        var projectIdSet = new HashSet<Long>();
        for (var analysisRun: analysisRuns) {
            var projectId = analysisRun.getProject().getId();
            if (!projectIdSet.contains(projectId)) {
                projectService.createUserProjectPermission(user,analysisRun.getServer(), analysisRun.getProject());
            }
            projectIdSet.add(projectId);
        }
    }

    public void updateProgress(AnalysisRun analysisRun, String message, Double progress, boolean saveToDatabase){
        analysisRun.setMessage(message);
        analysisRun.setProgress(progress);
        messageService.sendMessage(analysisRun);
        if(saveToDatabase == true){
            analysisRunRepository.save(analysisRun);
        }
    }

}
