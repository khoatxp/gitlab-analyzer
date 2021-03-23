package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.AnalysisRunProgress;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.RawTimeLineProjectData;
import com.eris.gitlabanalyzer.service.AnalyticsService;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.MessageService;
import com.eris.gitlabanalyzer.service.ProjectService;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final AnalyticsService analyticsService;
    private final AuthService authService;

    @Autowired
    public ProjectController(ProjectService projectService, AnalyticsService analyticsService, AuthService authService, MessageService messageService){
        this.projectService = projectService;
        this.analyticsService = analyticsService;
        this.authService = authService;
    }

    @GetMapping(path = "/{projectId}/rawdata")
    public RawTimeLineProjectData analyzeProject(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {
        return projectService.getTimeLineProjectData(projectId, startDateTime, endDateTime);
    }

    @GetMapping
    public List<Project> getProjects(){
        return projectService.getProjects();
    }

    @PostMapping(path = "/analytics/save_all")
    public List<Long> saveAllFromGitlab(
            @RequestBody List<AnalysisRunView> analysisRuns){
        List<Long> analysisRunIds = analysisRuns.stream().map(AnalysisRunView::getId).collect(Collectors.toList());
        return analyticsService.loadProjectDataForAnalysisRuns(analysisRunIds);
    }

    @PostMapping(path = "/analytics/generate_analysis_runs")
    public Stream<AnalysisRunView> generateAnalysisRuns(
            Principal principal,
            @RequestBody List<Long> projectIdList,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        var user = authService.getLoggedInUser(principal);
        return analyticsService.saveProjectsAndAnalysisRuns(user, projectIdList, startDateTime, endDateTime);
    }

}
