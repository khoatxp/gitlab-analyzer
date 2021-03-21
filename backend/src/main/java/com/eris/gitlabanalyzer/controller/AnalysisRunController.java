package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabProject;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.GitLabService;
import com.eris.gitlabanalyzer.service.UserServerService;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/v1/analysis_run")
public class AnalysisRunController {
    private final AnalysisRunRepository analysisRunRepository;
    private final AuthService authService;
    private final UserServerService userServerService;

    @Autowired
    public AnalysisRunController(AnalysisRunRepository analysisRunRepository, AuthService authService, UserServerService userServerService) {
        this.analysisRunRepository = analysisRunRepository;
        this.authService = authService;
        this.userServerService = userServerService;
    }

    @GetMapping("{serverId}")
    public Stream<AnalysisRunView> getAnalysisRunsByOwnerUserId(
            Principal currentUser,
            @PathVariable("serverId") Long serverId) {
        User user = this.authService.getLoggedInUser(currentUser);
        List<AnalysisRun> analysisRuns = this.analysisRunRepository.findByOwnerUserIdAndServerIdOrderByCreatedDateTimeDesc(user.getId(), serverId);
        return analysisRuns.stream().map(AnalysisRunView::fromAnalysisRun);
    }

    @GetMapping("{serverId}/all")
    public Stream<AnalysisRunView> getAccessibleAnalysisRuns(
            Principal currentUser,
            @PathVariable("serverId") Long serverId) {
        User user = this.authService.getLoggedInUser(currentUser);
        List<Long> userAccessibleGitlabProjectIds = getUserAccessibleGitlabProjectIds(user, serverId);
        List<AnalysisRun> analysisRuns = this.analysisRunRepository.findByServerAndGitLabProjectIds(serverId, userAccessibleGitlabProjectIds);
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
