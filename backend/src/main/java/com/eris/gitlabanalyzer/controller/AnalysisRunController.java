package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.AnalysisRunProgress;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.service.AnalysisRunService;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/v1/analysis_run")
public class AnalysisRunController {
    private final AnalysisRunRepository analysisRunRepository;
    private final AuthService authService;
    private final AnalysisRunService analysisRunService;

    @Autowired
    public AnalysisRunController(AnalysisRunRepository analysisRunRepository, AuthService authService, AnalysisRunService analysisRunService) {
        this.analysisRunRepository = analysisRunRepository;
        this.authService = authService;
        this.analysisRunService = analysisRunService;
    }

    @GetMapping("progress/{analysisRunId}")
    public AnalysisRunProgress getAnalysisRun(
            @PathVariable("analysisRunId") Long analysisRunId){
        return analysisRunService.getAnalysisRunProgress(analysisRunId);
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
        return this.analysisRunService.getAccessibleAnalysisRuns(user, serverId);
    }
}
