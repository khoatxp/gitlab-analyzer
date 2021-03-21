package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabProject;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnalysisRunService {
    private AnalysisRunRepository analysisRunRepository;
    private UserServerService userServerService;

    @Autowired
    public void AnalyticsService(AnalysisRunRepository analysisRunRepository, UserServerService userServerService) {
        this.analysisRunRepository = analysisRunRepository;
        this.userServerService = userServerService;
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
        return this.analysisRunRepository.save(analysisRun);
    }

    public Stream<AnalysisRunView> getAccessibleAnalysisRuns(User user, Long serverId) {
        List<Long> userAccessibleGitlabProjectIds = getUserAccessibleGitlabProjectIds(user, serverId);
        List<AnalysisRun> analysisRuns = this.analysisRunRepository.findOthersByServerIdAndGitLabProjectIds(user.getId(), serverId, userAccessibleGitlabProjectIds);
        return analysisRuns.stream().map(AnalysisRunView::fromAnalysisRun);
    }

    private List<Long> getUserAccessibleGitlabProjectIds(User user, Long serverId) {
        var userServer = userServerService.getUserServer(user, serverId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find server."));
        var gitLabService = new GitLabService(userServer.getServer().getServerUrl(), userServer.getAccessToken());
        List<GitLabProject> gitLabProjects = gitLabService
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
}
