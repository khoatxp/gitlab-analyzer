package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.RawTimeLineProjectData;
import com.eris.gitlabanalyzer.service.AnalyticsService;
import com.eris.gitlabanalyzer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final AnalyticsService analyticsService;
    @Autowired
    public ProjectController(ProjectService projectService, AnalyticsService analyticsService){
        this.projectService = projectService;
        this.analyticsService = analyticsService;
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

    @PostMapping(path = "/analytics")
    public List<Long> saveAllFromGitlab(
            @RequestBody List<Long> projectIdList,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return analyticsService.saveAllFromGitlab(projectIdList, startDateTime, endDateTime);
    }
}
